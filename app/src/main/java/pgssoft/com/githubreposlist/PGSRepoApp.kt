package pgssoft.com.githubreposlist

import android.app.Application


class PGSRepoApp : Application() {


    companion object {
        lateinit var app: PGSRepoApp

    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}