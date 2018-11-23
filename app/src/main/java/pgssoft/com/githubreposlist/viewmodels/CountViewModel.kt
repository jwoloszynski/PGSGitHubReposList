package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class CountViewModel : ViewModel() {

    val itemCount : MutableLiveData<Int> by lazy { MutableLiveData<Int>()}
}