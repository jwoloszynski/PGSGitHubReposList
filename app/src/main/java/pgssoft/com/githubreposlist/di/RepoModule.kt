package pgssoft.com.githubreposlist.di

import dagger.Module
import dagger.Provides
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.api.GHApiProvider
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import javax.inject.Singleton

@Module
class RepoModule(private val app: PGSRepoApp) {


    @Provides
    @Singleton
    fun provideGHApi(): GHApi {
        return GHApiProvider.getApi()
    }

    @Provides
    @Singleton
    fun provideDb(): ReposDatabase {

        return ReposDatabase.getInstance(context = app)
    }
}