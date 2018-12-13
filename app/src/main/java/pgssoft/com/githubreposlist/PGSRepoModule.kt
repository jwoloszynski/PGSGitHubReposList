package pgssoft.com.githubreposlist

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import pgssoft.com.githubreposlist.data.api.GHApiProvider
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.utils.PrefsHelper

val app_module = module {
    single { ReposDatabase.getInstance(androidContext()) }
    single { PrefsHelper(androidContext()) }
    single { GHApiProvider.getApi() }

}


