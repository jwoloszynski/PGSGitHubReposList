package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.net.ConnectivityManager
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    var selected: LiveData<Repository> = getRepoById(0)

    var compositeDisposable = CompositeDisposable()
    fun getRepoList() = repoListLiveData

    fun getRepoCount(): LiveData<Int> = repoListCount

    fun setSelected(id: Int) {
        selected = getRepoById(id)
    }

    fun onRefresh() {

        if (cm.activeNetworkInfo != null) {
            scope.launch {
                try {


                    val state: Flowable<RepoDownloadStatus> = repoRepository.fetchAll()
                    state.observeOn(Schedulers.io()).subscribe {
                        _refreshState.postValue(Event(it))
                    }
                }
                catch (e: Exception) {
                    Log.d("VIEWMODEL", "VM catched the exception")
                }
            }
        } else {

            _refreshState.postValue(Event(RepoDownloadStatus.ErrorNoConnection))
        }
    }

    fun clearRepoList() {
        scope.launch { repoRepository.clearRepoList() }
    }

    private fun getRepoById(id: Int): LiveData<Repository> {
        repository = repoRepository.getRepoById(id)
        return repository

    }

    fun update(id: Int, comment: String) {
        scope.launch { repoRepository.updateRepoComment(id, comment) }
    }

    override fun onCleared() {

        if(compositeDisposable != null) {
            compositeDisposable.dispose()
        }
    }
}