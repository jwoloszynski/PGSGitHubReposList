package pgssoft.com.githubreposlist.di

import android.content.Context
import dagger.Module
import dagger.Provides
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import javax.inject.Singleton

@Module
class PGSRepoModule(private val app: PGSRepoApp) {


    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    fun provideDb(): ReposDatabase {

        return ReposDatabase.getInstance(context = PGSRepoApp.app)
    }

}