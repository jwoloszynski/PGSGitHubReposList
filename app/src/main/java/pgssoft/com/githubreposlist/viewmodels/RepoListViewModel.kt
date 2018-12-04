package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper

class RepoListViewModel : ViewModel() {


    private val repo = RepoRepository()
    private var repoListLiveData: LiveData<List<Repository>> = MutableLiveData()
    private var repoListCount: LiveData<Int> = MutableLiveData()
    private var repoListCountText: LiveData<String>
    private var _repoListStatusText: MutableLiveData<String> = MutableLiveData()
    val repoListStatusText: LiveData<String>
        get() = _repoListStatusText

    private lateinit var prefs: PrefsHelper

    private val compositeDisposable: CompositeDisposable


    init {
        repoListLiveData = repo.getRepoList()
        repoListCount = repo.getCount()
        repoListCountText = getRepoCountText()
        compositeDisposable = CompositeDisposable()

    }

    fun getRepoList() = repoListLiveData

    fun getRepoCountText(): LiveData<String> {

        return Transformations.map(repoListCount) { count ->
            getCountText(count)
        }

    }

    fun onRefresh(itemCount: Int) {

        if (canRefreshList(itemCount)) {

            var d = repo.fetchAll()
            compositeDisposable.add(d.subscribe {
                _repoListStatusText.value = it
            })


        } else {
            _repoListStatusText.value = ""
        }

    }

    fun clearRepoList() {

        compositeDisposable.add(repo.clearRepoList().subscribe())

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

    override fun onCleared() = compositeDisposable.dispose()

}