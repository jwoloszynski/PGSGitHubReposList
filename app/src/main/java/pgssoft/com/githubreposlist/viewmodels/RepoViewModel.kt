package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel: ViewModel(){

    private val repoRepository = RepoRepository.getRepoInstance()
    private var repository: LiveData<Repository>

    init {
        repository = repoRepository.getRepoById(1)
    }

    fun getRepo(id:Int){
        repository = repoRepository.getRepoById(id)
    }

}