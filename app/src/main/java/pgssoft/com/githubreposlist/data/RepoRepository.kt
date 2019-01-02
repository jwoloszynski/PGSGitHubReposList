package pgssoft.com.githubreposlist.data

import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.di.AppModule
import pgssoft.com.githubreposlist.di.DaggerRepoComponent
import pgssoft.com.githubreposlist.utils.PrefsHelper
import javax.inject.Inject


class RepoRepository {

    init {
        DaggerRepoComponent.builder().appModule(AppModule(PGSRepoApp.app)).build().inject(this)
    }

    companion object {
        private const val orgName = "PGSSoft"

    }

    @Inject
    lateinit var db: ReposDatabase
    @Inject
    lateinit var api: GHApi
    @Inject
    lateinit var prefs: PrefsHelper


    fun fetchAll(): RepoDownloadStatus {

        if (canRefreshList()) {

            try {
                val response = api.getOrganizationRepos(orgName).execute()
                return if (response.body() == null) {
                    RepoDownloadStatus.Forbidden
                } else {

                    val repoList = response.body()!!

                    if (!repoList.isNullOrEmpty()) {
                        for (repo in repoList) {
                            val comment = getCommentByRepoId(repo.id)
                            repo.comment = comment?.comment ?: ""
                        }
                    }
                    db.repoDao().insertAll(repoList)

                    RepoDownloadStatus.DataOk
                }
            } catch (e: Exception) {
                return RepoDownloadStatus.ErrorMessage(e.message.toString())
            }


        } else {
            return RepoDownloadStatus.NoRefreshDueToTime
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