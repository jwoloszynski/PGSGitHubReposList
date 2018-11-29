package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel : ViewModel() {

    private val repoRepositoryObservable = RepoRepository.getRepoInstance()
    private lateinit var repoRepository: RepoRepository

    private var repository: LiveData<Repository>

    init {
        repoRepositoryObservable.doOnNext{repo -> repoRepository=repo}.subscribe()
        repository = repoRepository.getRepoById(68371586)
    }

    fun getRepo() = repository

    fun getRepoById(id: Int){
        repository = repoRepository.getRepoById(id)
    }


}