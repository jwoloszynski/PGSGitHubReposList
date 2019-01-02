package pgssoft.com.githubreposlist.di

import dagger.Module
import dagger.Provides
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.utils.PrefsHelper
import javax.inject.Singleton

@Module
class AppModule(private val app: PGSRepoApp) {


    @Provides
    @Singleton
    fun providePrefs(): PrefsHelper{
        return PrefsHelper(app)
    }



}