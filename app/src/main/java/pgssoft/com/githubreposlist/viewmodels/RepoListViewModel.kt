package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.EventObserver
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.RepoStatus
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper

class RepoListViewModel : ScopedViewModel() {


    private val repoRepository = RepoRepository()
    private var repoListLiveData: LiveData<List<Repository>>
    private var repoListCount: LiveData<Int>
    private var repoListCountText: LiveData<String>
    private var _repoListErrorText: MutableLiveData<String> = MutableLiveData()
    val repoListErrorText: LiveData<String>
        get() = _repoListErrorText
   var statusLiveData: LiveData<Event<RepoStatus>>


    val prefs = PrefsHelper(PGSRepoApp.app)


    init {


        repoListLiveData = repoRepository.getRepoList()
        repoListCount = repoRepository.getCount()
        repoListCountText = getRepoCountText()
        statusLiveData = repoRepository._refreshState

    }

    fun getRepoList() = repoListLiveData

    fun getRepoCountText(): LiveData<String> {

        return Transformations.map(repoListCount) { count ->
            getCountText(count)
        }

    }

    fun onRefresh(itemCount: Int) {
        if (canRefreshList(itemCount)) {

            scope.launch{repoRepository.fetchAll() }

        }

    }

    fun clearRepoList() {


        scope.launch{  repoRepository.clearRepoList() }
        prefs.clearAll()

    }


    private fun getCountText(count: Int): String {

        return if (count > 0) {
            count.toString()
        } else {
            "Pull to refresh"
        }


    }


    private fun canRefreshList(itemCount: Int): Boolean {


        val timeRefreshed = prefs.time
        val timeBetween = System.currentTimeMillis() - timeRefreshed

        if ((timeRefreshed == -1L) or (timeBetween > (1 * 60 * 1000)) or (itemCount < 1)) {
            prefs.time = System.currentTimeMillis()
            return true
        }
        return false


    }
}

