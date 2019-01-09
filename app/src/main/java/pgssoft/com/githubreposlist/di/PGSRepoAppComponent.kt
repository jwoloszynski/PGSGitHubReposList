package pgssoft.com.githubreposlist.di

import dagger.Component
import pgssoft.com.githubreposlist.ui.RepoDetailFragment
import pgssoft.com.githubreposlist.ui.ReposActivity
import pgssoft.com.githubreposlist.ui.RepoListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface PGSRepoAppComponent {

    fun inject(detailFragment: RepoDetailFragment)
    fun inject(listFragment: RepoListFragment)
    fun inject(activity: ReposActivity)

}
