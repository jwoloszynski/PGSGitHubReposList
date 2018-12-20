package pgssoft.com.githubreposlist

import android.app.Application
import pgssoft.com.githubreposlist.di.DaggerPGSRepoAppComponent
import pgssoft.com.githubreposlist.di.PGSRepoAppComponent
import pgssoft.com.githubreposlist.di.AppModule


class PGSRepoApp : Application() {

    lateinit var appComponent: PGSRepoAppComponent
    companion object {
        lateinit var app: PGSRepoApp

    }

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
        app = this
    }

    private fun initDagger(app:PGSRepoApp): PGSRepoAppComponent =
            DaggerPGSRepoAppComponent.builder().appModule(AppModule(app)).build()


}