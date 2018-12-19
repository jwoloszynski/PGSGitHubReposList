package pgssoft.com.githubreposlist.di

import dagger.Component
import pgssoft.com.githubreposlist.ui.RepoDetailFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [PGSRepoModule::class])
interface PGSRepoAppComponent {

fun inject(detailFragment: RepoDetailFragment)

}