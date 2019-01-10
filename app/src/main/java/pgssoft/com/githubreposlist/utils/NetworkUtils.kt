package pgssoft.com.githubreposlist.utils

import android.net.ConnectivityManager

class NetworkUtils(private val cm: ConnectivityManager) {


    fun isConnection(): Boolean {
        return cm.activeNetworkInfo != null
    }
}
