package pgssoft.com.githubreposlist.utils

import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(private val cm: ConnectivityManager) {


    fun isConnection(): Boolean {
        return cm.activeNetworkInfo != null
    }
}
