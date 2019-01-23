package pgssoft.com.githubreposlist.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.api.GHApiProvider
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.utils.NetworkUtils
import pgssoft.com.githubreposlist.utils.PrefsHelper
import javax.inject.Singleton

@Module
class ServiceModule(private val context: Context) {


    @Provides
    @Singleton
    fun providePrefs(): PrefsHelper {
        return PrefsHelper(context)
    }

    @Provides
    @Singleton
    fun provideGHApi(): GHApi {
        return GHApiProvider.getApi()
    }

    @Provides
    @Singleton
    fun provideDb(): ReposDatabase {
        return ReposDatabase.getInstance(context = context)
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
    fun provideConnectivityManager(): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(cm: ConnectivityManager): NetworkUtils {
        return NetworkUtils(cm)
    }
}
