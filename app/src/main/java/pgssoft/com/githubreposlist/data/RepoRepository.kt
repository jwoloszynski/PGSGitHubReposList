package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.utils.PrefsHelper


class RepoRepository : KoinComponent {

    private val api: GHApi by inject()
    private val db: ReposDatabase by inject()
    private val prefs: PrefsHelper by inject()

    companion object {
        private const val orgName = "PGSSoft"

    }

    private var _refreshState = MutableLiveData<Event<RepoDownloadStatus>>()
    val refreshState: LiveData<Event<RepoDownloadStatus>>
        get() = _refreshState

    var isInternetConnection: Boolean = false


    fun fetchAll() {

        if (!isInternetConnection) {
            _refreshState.postValue(Event(RepoDownloadStatus.Error(PGSRepoApp.app.getString(R.string.no_internet_connection))))
            return
        }

        if (canRefreshList()) {

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