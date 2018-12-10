package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.utils.PrefsHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RepoRepository {

    private var _refreshState = MutableLiveData<Event<RepoDownloadStatus>>()

    val refreshState: LiveData<Event<RepoDownloadStatus>>
        get() = _refreshState

    private val client: OkHttpClient =
        OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build()

    private val api: GHApi =
        Retrofit.Builder().baseUrl(PGSRepoApp.app.getString(R.string.gh_api_url)).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(GHApi::class.java)

    private val db = ReposDatabase.getInstance(PGSRepoApp.app)
    private val prefs = PrefsHelper(PGSRepoApp.app)
    private val orgName = PGSRepoApp.app.getString(R.string.pgs_gh_org_name)
    private val cm = PGSRepoApp.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    fun fetchAll() {

        if (canRefreshList()) {

            if (cm.activeNetworkInfo == null) {
                _refreshState.postValue(Event(RepoDownloadStatus.Error(PGSRepoApp.app.getString(R.string.no_internet_connection))))
                return
            }
            try {
                val response = api.getOrganizationRepos(orgName).execute()
                if (response.body() == null) {
                    _refreshState.postValue(Event(RepoDownloadStatus.Error(PGSRepoApp.app.getString(R.string.rate_limit_exceeded))))
                } else {

                    val repoList = response.body()!!

                    if (!repoList.isNullOrEmpty()) {
                        for (repo in repoList) {
                            var comment = getCommentByRepoId(repo.id)
                            repo.comment = comment?.comment ?: ""
                        }
                    }
                    db.repoDao().insertAll(repoList)
                    _refreshState.postValue(Event(RepoDownloadStatus.DataOk))
                }
            } catch (e: Exception) {
                _refreshState.postValue(Event(RepoDownloadStatus.Error(e.message.toString())))
            }
        } else {
            _refreshState.postValue(Event(RepoDownloadStatus.DataOk))
        }
    }


    fun getRepoList() = db.repoDao().getAll()
    fun getRepoById(id: Int) = db.repoDao().get(id)
    fun getCount() = db.repoDao().getCount()

    fun clearRepoList() {
        db.repoDao().deleteAll()
        prefs.clearAll()
    }

    fun updateRepo(id: Int, comment: String) {
        db.repoDao().updateRepoComment(id, comment)

    }

    private fun getItemListCount() = db.repoDao().getListCount()
    private fun getCommentByRepoId(repoId: Int) = db.repoDao().getCommentByRepoId(repoId)

    private fun canRefreshList(): Boolean {

        val timeRefreshed = prefs.time
        val timeBetween = System.currentTimeMillis() - timeRefreshed

        if ((timeRefreshed == -1L) or (timeBetween > (1 * 60 * 1000)) or (getItemListCount() == 0)) {
            prefs.time = System.currentTimeMillis()
            return true
        }
        return false


    }

}