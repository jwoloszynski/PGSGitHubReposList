package pgssoft.com.githubreposlist.data

import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.db.ReposDatabase

class RepoRepository {

    companion object {

        private var instance: RepoRepository? = null

        fun getInstance(): RepoRepository {
            if (instance == null) {
                instance = RepoRepository()
            }
            return instance!!

        }
    }

    private val db = ReposDatabase.getInstance(PGSRepoApp.app)

    fun getRepoList() = db.repoDao().getAll()

    fun gerRepoById(id: Int) = db.repoDao().get(id)

    fun getCount() = db.repoDao().getCount()


}
