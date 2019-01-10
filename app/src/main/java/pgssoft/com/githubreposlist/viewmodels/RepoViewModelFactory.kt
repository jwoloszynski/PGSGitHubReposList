package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.utils.NetworkUtils

class RepoViewModelFactory(private val utils: NetworkUtils, private val repoRepository: RepoRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RepoViewModel(utils, repoRepository) as T
    }
}
