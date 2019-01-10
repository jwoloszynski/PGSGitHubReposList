package pgssoft.com.githubreposlist.di

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.api.GHApiProvider
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.utils.NetworkUtils
import pgssoft.com.githubreposlist.utils.PrefsHelper
import pgssoft.com.githubreposlist.viewmodels.RepoViewModelFactory
import javax.inject.Singleton

@Module
class AppModule(private val app: PGSRepoApp) {


    @Provides
    @Singleton
    fun providePrefs(): PrefsHelper {
        return PrefsHelper(app)
    }

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


    @Provides
    @Singleton
    fun provideConnectivityManager(): ConnectivityManager {
        return app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(cm: ConnectivityManager): NetworkUtils {
        return NetworkUtils(cm)
    }

    @Provides
    @Singleton
    fun provideResources(): Resources {
        return app.resources
    }

    @Provides
    @Singleton
    fun provideRepository(
        api: GHApi,
        db: ReposDatabase,
        prefs: PrefsHelper
    ): RepoRepository {
        return RepoRepository(api, db, prefs)
    }

    @Provides
    @Singleton
    fun provideRepoViewModelFactory(
        utils: NetworkUtils,
        repository: RepoRepository
    ): RepoViewModelFactory {
        return RepoViewModelFactory(utils, repository)
    }


}
