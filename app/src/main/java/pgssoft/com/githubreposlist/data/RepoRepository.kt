package pgssoft.com.githubreposlist.data

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRepository @Inject constructor(
    private val api: GHApi,
    private val db: ReposDatabase,
    private val prefs: PrefsHelper
) {

    companion object {
        private const val orgName = "PGSSoft"
    }


    fun fetchAll(): Single<RepoDownloadStatus> {
        return if (canRefreshList()) {
            api.getOrganizationRepos(orgName).map { response ->

                when {
                    response.isSuccessful -> {
                        insertRepos(response.body()!!)
                        RepoDownloadStatus.DataOk
                    }
                    response.code() == HttpURLConnection.HTTP_FORBIDDEN -> RepoDownloadStatus.Forbidden
                    else -> RepoDownloadStatus.ErrorMessage(response.errorBody().toString())
                }
            }
        } else {
            Single.just(RepoDownloadStatus.NoRefreshDueToTime)
        }
    }

    fun getRepoList() = db.repoDao().getAll()

    fun getRepoById(id: Int) = db.repoDao().get(id)

    fun clearRepoList() {
        db.repoDao().deleteAll()
        prefs.clearAll()
    }

    fun updateRepo(repo: Repository) {
        db.repoDao().update(repo)
    }

    fun updateRepoComment(id: Int, comment: String) {
        db.repoDao().updateRepoComment(id, comment)
    }

    private fun getItemListCount() = db.repoDao().getListCount().subscribeOn(Schedulers.io()).blockingGet()
    private fun getLocalDetails(repoId: Int) =
        db.repoDao().getLocalDetailsById(repoId).subscribeOn(Schedulers.io()).blockingGet()

    private fun canRefreshList(): Boolean {
        val timeRefreshed = prefs.time
        val timeBetween = System.currentTimeMillis() - timeRefreshed

        if ((timeRefreshed == -1L) or (timeBetween > (1 * 60 * 1000)) or (getItemListCount() <= 0)) {
            prefs.time = System.currentTimeMillis()
            return true
        }
        return false
    }

    private fun insertRepos(repoList: List<Repository>) {

        if (!repoList.isNullOrEmpty()) {
            for (repo in repoList) {
                val details = getLocalDetails(repo.id)
                repo.comment = details?.comment ?: ""
                repo.liked = details?.liked
            }

        }

        db.repoDao().insertAll(repoList)
    }

}
