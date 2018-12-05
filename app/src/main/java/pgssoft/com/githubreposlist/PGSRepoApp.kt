package pgssoft.com.githubreposlist

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class PGSRepoApp : Application() {


    companion object {
        lateinit var app: PGSRepoApp

    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }

}