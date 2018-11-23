package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoListViewModel : ViewModel() {


    val repoList = MutableLiveData<List<Repository>>()
    private val repoRepository = RepoRepository.getInstance()
    private val repoCount = MutableLiveData<Int>()


    fun getRepoList() = repoRepository.getRepoList()
    fun getRepoCount() = repoRepository.getCount()

}