package pgssoft.com.githubreposlist.di

import dagger.Component
import pgssoft.com.githubreposlist.data.RepoRepository
import javax.inject.Singleton


@Singleton
@Component (modules = [AppModule::class, RepoModule::class])
interface RepoComponent {
    fun inject(repoRepository: RepoRepository)
}