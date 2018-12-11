package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import kotlinx.coroutines.launch
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository

class RepoViewModel : ScopedViewModel() {

    private val repoRepository = RepoRepository()

    private var repoListLiveData: LiveData<List<Repository>>
    private var repoListCount: LiveData<Int>
    private var repoListCountText: LiveData<String>
    var statusLiveData: LiveData<Event<RepoDownloadStatus>>


    var repository: LiveData<Repository>

    init {

        repoListLiveData = repoRepository.getRepoList()
        repoListCount = repoRepository.getCount()
        repoListCountText = getRepoCountText()
        statusLiveData = repoRepository.refreshState
        repository = repoRepository.getRepoById(0)


    }


    fun getRepoList() = repoListLiveData

    fun getRepoCountText(): LiveData<String> {

        return Transformations.map(repoListCount) { count ->
            getCountText(count)
        }

    }

    fun onRefresh() {

        scope.launch { repoRepository.fetchAll() }

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

}