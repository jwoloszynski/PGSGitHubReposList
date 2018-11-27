package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.ReposFetcher
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper

class RepoListViewModel : ViewModel() {


    private val repoRepository = RepoRepository

    private var repoListLiveData: LiveData<List<Repository>>
    private var repoListCount: LiveData<Int>
    private var repoListCountText: LiveData<String>
    private var _repoListErrorText: MutableLiveData<String> = MutableLiveData()
    val repoListErrorText: LiveData<String>
        get() = _repoListErrorText

    private lateinit var prefs: PrefsHelper
    private val fetcher = ReposFetcher()

    init {
        repoListLiveData = repoRepository.getRepoList()
        repoListCount = repoRepository.getCount()
        repoListCountText = getRepoCountText()

    }

    fun getRepoList() = repoListLiveData

    fun getRepoCountText(): LiveData<String> {

        return Transformations.map(repoListCount) { count ->
            getCountText(count)
        }

    }


    fun onRefresh(itemCount: Int) {
        prefs = PrefsHelper(PGSRepoApp.app)
        val timeRefreshed = prefs.time
        val timeBetween = System.currentTimeMillis() - timeRefreshed
        if ((timeRefreshed == -1L) or (timeBetween > (1 * 60 * 1000)) or (itemCount < 1)) {
            fetcher.fetchAll { error -> _repoListErrorText.value = error }
            prefs.time = System.currentTimeMillis()
        } else {
            _repoListErrorText.value = null
        }

    }

    fun clearRepoList() {

        fetcher.clearRepoList()

    }


    private fun getCountText(count: Int): String {

        return if (count > 0) {
            count.toString()
        } else {
            "Pull to refresh"
        }


    }


}