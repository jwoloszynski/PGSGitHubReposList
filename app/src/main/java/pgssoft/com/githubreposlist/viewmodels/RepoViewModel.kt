package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel : ViewModel() {

    private val repo = RepoRepository()

    private var repository: LiveData<Repository>

    init {
       repository = repo.getRepoById(1)

    }

    fun getRepo() = repository

    fun getRepoById(id: Int):RepoViewModel {
        repository = repo.getRepoById(id)
        return this
    }



//    fun update (id: Int, comment: String) {
//        repo.updateRepo(id,comment)
//    }
//
    fun update (repository: Repository?) {
        repo.updateRepo(repository)
    }

}