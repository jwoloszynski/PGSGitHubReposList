package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import kotlinx.coroutines.launch
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper

class RepoListViewModel : ScopedViewModel() {

    private val repoRepository = RepoRepository()
    private var repoListLiveData: LiveData<List<Repository>>
    private var repoListCount: LiveData<Int>
    private var repoListCountText: LiveData<String>
    var statusLiveData: LiveData<Event<RepoDownloadStatus>>


    init {

        repoListLiveData = repoRepository.getRepoList()
        repoListCount = repoRepository.getCount()
        repoListCountText = getRepoCountText()
        statusLiveData = repoRepository.refreshState

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
            "Pull to refresh"
        }


    }

}


