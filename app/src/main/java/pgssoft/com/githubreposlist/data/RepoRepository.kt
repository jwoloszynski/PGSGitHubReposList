package pgssoft.com.githubreposlist.data

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.utils.PrefsHelper
import java.lang.Exception
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


    fun fetchAll(): Flowable<RepoDownloadStatus> {

        try {
            val d = api.getOrganizationRepos(orgName)
                .doOnNext {

                    if (!it.list.isNullOrEmpty()) {
                        db.repoDao().insertAll(it.list)
                    }
                }.map {
                    it.status
                }
                .doOnError {
                    if (it!=null) {
                        it.message.toString()
                        throw it
                    }
                    else {

                    }
                }


                .subscribeOn(Schedulers.io())



            return d
        } catch (e: Exception) {
        }
        return Flowable.empty()
    }


//
//        if (canRefreshList()) {
//            try {
//                val response = api.getOrganizationRepos(orgName).execute()
//                return if (response.body() == null) {
//                    RepoDownloadStatus.Forbidden
//                } else {
//                    val repoList = response.body()!!
//                    if (!repoList.isNullOrEmpty()) {
//                        for (repo in repoList) {
//                            val comment = getCommentByRepoId(repo.id)
//                            repo.comment = comment?.comment ?: ""
//                        }
//                    }
//                    db.repoDao().insertAll(repoList)
//                    RepoDownloadStatus.DataOk
//                }
//            } catch (e: Exception) {
//                return RepoDownloadStatus.ErrorMessage(e.message.toString())
//            }
//
//        } else {
//            return RepoDownloadStatus.NoRefreshDueToTime
//        }
//


    fun getRepoList() = db.repoDao().getAll()
    fun getRepoById(id: Int) = db.repoDao().get(id)
    fun getCount() = db.repoDao().getCount()

    fun clearRepoList() {
        db.repoDao().deleteAll()
        prefs.clearAll()
    }

    fun updateRepoComment(id: Int, comment: String) {
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