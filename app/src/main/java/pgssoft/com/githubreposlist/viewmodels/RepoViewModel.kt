package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.ConnectivityManager
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel(private val cm: ConnectivityManager, private val repoRepository: RepoRepository) :
    ViewModel() {

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
            compositeDisposable.add(
                repoRepository.fetchAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnEvent { status, _ -> _refreshState.postValue(Event(status)) }
                    .subscribe())
        } else {
            _refreshState.postValue(Event(RepoDownloadStatus.ErrorNoConnection))
        }
    }

    fun clearRepoList() {
        compositeDisposable.add(
            Completable.fromAction { repoRepository.clearRepoList() }.subscribeOn(Schedulers.io()).subscribe()
        )
    }

    private fun getRepoById(id: Int): LiveData<Repository> {
        repository = repoRepository.getRepoById(id)
        return repository

    }

    fun update(id: Int, comment: String) {
        compositeDisposable.add(
            Completable.fromAction { repoRepository.updateRepoComment(id, comment) }
                .subscribeOn(Schedulers.io()).subscribe())
    }

    override fun onCleared() {

        if (compositeDisposable != null) {
            compositeDisposable.dispose()
        }
    }
}
