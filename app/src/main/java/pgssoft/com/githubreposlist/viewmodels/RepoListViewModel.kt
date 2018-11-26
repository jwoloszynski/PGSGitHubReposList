package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoListViewModel : ViewModel() {


    private val repoRepository = RepoRepository.getInstance()

    fun getRepoList() = repoRepository.getRepoList()
    fun getRepoCount() = repoRepository.getCount()

}