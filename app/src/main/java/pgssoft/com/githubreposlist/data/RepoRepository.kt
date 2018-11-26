package pgssoft.com.githubreposlist.data

import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.db.ReposDatabase

object RepoRepository {


    private val db = ReposDatabase.getInstance(PGSRepoApp.app)

    fun getRepoList() = db.repoDao().getAll()

    fun gerRepoById(id: Int) = db.repoDao().get(id)

    fun getCount() = db.repoDao().getCount()


}
