package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pgssoft.com.githubreposlist.data.db.Repository

class ReposListViewModel : ViewModel() {

    private lateinit var repoListLiveData : MutableLiveData<List<Repository>>

    fun getRepositories(): MutableLiveData<List<Repository>>{

        if (!::repoListLiveData.isInitialized){
            repoListLiveData = MutableLiveData<List<Repository>>()
            getRepos()
        }
        return repoListLiveData;

    }

    private fun getRepos(){




    }


}
