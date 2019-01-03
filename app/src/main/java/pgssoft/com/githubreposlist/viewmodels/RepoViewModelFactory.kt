package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.net.ConnectivityManager
import pgssoft.com.githubreposlist.data.RepoRepository

class RepoViewModelFactory(private val cm: ConnectivityManager, private val repoRepository: RepoRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return RepoViewModel(cm, repoRepository) as T


    }
}