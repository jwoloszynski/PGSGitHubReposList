package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RepoRepository {

    var _refreshState = MutableLiveData<Event<RepoStatus>>()
    val refreshState: LiveData<Event<RepoStatus>>
        get() = _refreshState


    private val api: GHApi =
        Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create()).build()
            .create(GHApi::class.java)
    val db = ReposDatabase.getInstance(PGSRepoApp.app)


    fun fetchAll() {
        var orgName = PGSRepoApp.app.getString(R.string.pgsghorgname)

        try {
            val response = api.getOrganizationRepos(orgName).execute()
            if (response.body() == null) {
                Log.d("DEBUG", response.raw().message())
                _refreshState.postValue(Event(RepoStatus.Error("Rate limit exceeded")))
            } else {

                var repoList = response.body()!!

                repoList = repoList.sortedByDescending { it.pushed_at }
                for (repo in repoList) {
                    var comment = getCommentByRepoId(repo.id)
                    repo.comment = comment?.comment ?: ""


                }

                db.repoDao().insertAll(repoList)
                _refreshState.postValue(Event(RepoStatus.DataOk()))

            }
        } catch (e: Exception) {
            _refreshState.postValue(Event(RepoStatus.Error(e.message.toString())))
        }
    }


    fun getRepoList() = db.repoDao().getAll()

    fun getRepoById(id: Int) = db.repoDao().get(id)
    fun getCount() = db.repoDao().getCount()
    fun getCommentByRepoId(repoId: Int) = db.repoDao().getCommentByRepoId(repoId)


    fun clearRepoList() {
        db.repoDao().deleteAll()
    }

    fun updateRepo(id: Int, comment: String) {
        db.repoDao().updateRepoComment(id, comment)

    }


}