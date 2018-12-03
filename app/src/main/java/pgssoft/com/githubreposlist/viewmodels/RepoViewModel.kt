package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel : ViewModel() {

    private val repo = RepoRepository()

    private lateinit var repository: LiveData<Repository>

    init {
        repo.getRepoById(1)

    }

    fun getRepo() = repository

    fun getRepoById(id: Int) {
        repo.getRepoById(id)
    }


}