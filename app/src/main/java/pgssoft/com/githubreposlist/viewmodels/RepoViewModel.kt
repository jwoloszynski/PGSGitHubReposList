package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.net.ConnectivityManager
import kotlinx.coroutines.launch
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository

import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel(private val cm: ConnectivityManager, private val repoRepository: RepoRepository) :
    ScopedViewModel() {

    private var repoListLiveData: LiveData<List<Repository>> = repoRepository.getRepoList()

    private var _refreshState = MutableLiveData<Event<RepoDownloadStatus>>()
    val refreshState: LiveData<Event<RepoDownloadStatus>>
        get() = _refreshState

    var repoListCount = repoRepository.getCount()

    var repository = getRepoById(0)

    var selected = getRepoById(0)

    fun getRepoList() = repoListLiveData

    fun getRepoCount(): LiveData<Int> = repoListCount

    fun setSelected(id: Int) {
        selected = getRepoById(id)
    }

    fun onRefresh() {

        if (cm.activeNetworkInfo != null) {
            scope.launch {
                val state = repoRepository.fetchAll()
                setState(state)
            }
        } else {
            setState(RepoDownloadStatus.ErrorNoConnection)
        }
    }

    fun clearRepoList() {

        scope.launch { repoRepository.clearRepoList() }

    }

    fun getRepoById(id: Int): LiveData<Repository> {
        repository = repoRepository.getRepoById(id)
        return repository

    }

    fun update(id: Int, comment: String) {
        scope.launch { repoRepository.updateRepo(id, comment) }
    }

    private fun setState(state: RepoDownloadStatus) {

        val event = Event(state)
        _refreshState.postValue(event)
    }


}