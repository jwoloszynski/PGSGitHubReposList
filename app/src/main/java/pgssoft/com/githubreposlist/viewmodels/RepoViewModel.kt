package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository


class RepoViewModel : ScopedViewModel(), KoinComponent {

    val db: ReposDatabase by inject()

    private val repoRepository = RepoRepository()

    private var repoListLiveData: LiveData<List<Repository>>
    private var repoListCount: LiveData<Int>
    private var repoListCountText: LiveData<String>

    private var _refreshState = MutableLiveData<Event<RepoDownloadStatus>>()
    val refreshState: LiveData<Event<RepoDownloadStatus>>
        get() = _refreshState

    var cm = PGSRepoApp.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    
    var repository: LiveData<Repository>

    init {

        repoListLiveData = repoRepository.getRepoList()
        repoListCount = repoRepository.getCount()
        repoListCountText = getRepoCountText()
        repository = repoRepository.getRepoById(0)

    }


    fun getRepoList() = repoListLiveData

    fun getRepoCountText(): LiveData<String> {

        return Transformations.map(repoListCount) { count ->
            getCountText(count)
        }

    }

    fun onRefresh() {
        if (cm.activeNetworkInfo != null) {
            scope.launch {
                val state = repoRepository.fetchAll()
                setState(state)
            }
        } else {
            setState(RepoDownloadStatus.ErrorMessage(PGSRepoApp.app.getString(R.string.no_internet_connection)))
        }
    }

    fun clearRepoList() {

        scope.launch { repoRepository.clearRepoList() }

    }

    private fun getCountText(count: Int): String {

        return if (count > 0) {
            count.toString()
        } else {

            PGSRepoApp.app.getString(R.string.pull_to_refresh)
        }

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