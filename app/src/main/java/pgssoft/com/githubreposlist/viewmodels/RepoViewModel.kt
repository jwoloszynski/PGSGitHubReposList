package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.NetworkUtils

/**
 * ViewModel for repos, used in ReposActivity and its Fragments
 */

class RepoViewModel(private val networkUtils: NetworkUtils, private val repoRepository: RepoRepository) :
    ViewModel() {

    private var repoListLiveData: LiveData<List<Repository>> = repoRepository.getRepoList()

    private var _refreshState = MutableLiveData<Event<RepoDownloadStatus>>()
    private var compositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null

    val refreshState: LiveData<Event<RepoDownloadStatus>>
        get() = _refreshState

    var repository = getRepoById(0)

    var selected: LiveData<Repository> = getRepoById(0)
    fun getRepoList() = repoListLiveData

    fun setSelected(id: Int) {
        selected = getRepoById(id)
    }

    fun onRefresh() {

        if (networkUtils.isConnection()) {
            disposable?.dispose()

            disposable =
                    repoRepository.fetchAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(

                            { status -> _refreshState.postValue(Event(status)) },

                            { error ->
                                _refreshState.postValue(Event(RepoDownloadStatus.ErrorMessage(error.message.toString())))

                            })

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


        disposable?.dispose()
        compositeDisposable.clear()

    }

}
