package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel : ViewModel() {

    private val repo = RepoRepository()

    private lateinit var repository: LiveData<Repository>


    fun getRepoById(id: Int): LiveData<Repository> {
      repository = repo.getRepoById(id)
        return repository
    }


}