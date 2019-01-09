package pgssoft.com.githubreposlist.viewmodels

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * base ViewModel with CoroutineScope
 */

abstract class ScopedViewModel : ViewModel() {
    private val job = Job()
    protected val scope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
