package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper

class RepoListViewModel : ViewModel() {


    private val repoRepositoryObservable = RepoRepository.getRepoInstance()
    private var repoListLiveData: LiveData<List<Repository>> = MutableLiveData()
    private var repoListCount: LiveData<Int> = MutableLiveData()
    private var repoListCountText: LiveData<String>
    private var _repoListErrorText: MutableLiveData<String> = MutableLiveData()
    val repoListErrorText: LiveData<String>
        get() = _repoListErrorText

    private lateinit var prefs: PrefsHelper


    init {
        repoRepositoryObservable
            .subscribe { repo -> repoListLiveData = repo.getRepoList() }
        repoRepositoryObservable.subscribe { repo -> repoListCount = repo.getCount() }
        repoListCountText = getRepoCountText()

    }

    fun getRepoList() = repoListLiveData

    fun getRepoCountText(): LiveData<String> {

        return Transformations.map(repoListCount) { count ->
            getCountText(count)
        }

    }

    fun onRefresh(itemCount: Int) {
        if (canRefreshList(itemCount)) {
            repoRepositoryObservable.subscribe { repo ->
                repo.fetchAll(_repoListErrorText)
            }
        } else {
            _repoListErrorText.value = ""
        }

    }

    fun clearRepoList() {

        repoRepositoryObservable
            .subscribe { repo -> repo.clearRepoList() }

    }

    private fun getCountText(count: Int): String {

        return if (count > 0) {
            count.toString()
        } else {
            "Pull to refresh"
        }


    }

    private fun canRefreshList(itemCount: Int): Boolean {

        prefs = PrefsHelper(PGSRepoApp.app)
        val timeRefreshed = prefs.time
        val timeBetween = System.currentTimeMillis() - timeRefreshed

        if ((timeRefreshed == -1L) or (timeBetween > (1 * 60 * 1000)) or (itemCount < 1)) {
            prefs.time = System.currentTimeMillis()
            return true
        }
        return false


    }

}