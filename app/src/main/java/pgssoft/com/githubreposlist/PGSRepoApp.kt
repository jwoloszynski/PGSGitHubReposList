package pgssoft.com.githubreposlist

import android.app.Application
import org.koin.android.ext.android.startKoin


class PGSRepoApp : Application() {


    companion object {
        lateinit var app: PGSRepoApp

    }

    override fun onCreate() {
        super.onCreate()
        app = this
        startKoin(androidContext = this, modules = listOf(app_module))

    }


}