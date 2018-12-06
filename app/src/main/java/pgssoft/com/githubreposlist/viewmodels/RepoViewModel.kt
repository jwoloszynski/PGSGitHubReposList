package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.launch
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel : ScopedViewModel() {

    private val repo = RepoRepository()

    var repository: LiveData<Repository>


    init {
        repository = repo.getRepoById(0)
    }

    fun getRepoById(id: Int): LiveData<Repository> {
        repository = repo.getRepoById(id)
        return repository

    }

    fun update(id: Int, comment: String) {
        scope.launch { repo.updateRepo(id, comment) }
    }

}