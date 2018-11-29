package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel : ViewModel() {

    private val repoRepositoryObservable = RepoRepository.getRepoInstance()

    private lateinit var repository: LiveData<Repository>

    init {
        repoRepositoryObservable.doOnNext{repo -> repository=repo.getRepoById(1)}.subscribe()

    }

    fun getRepo() = repository

    fun getRepoById(id: Int){
        repoRepositoryObservable.doOnNext{repo -> repository = repo.getRepoById(id)}.subscribe()
    }


}