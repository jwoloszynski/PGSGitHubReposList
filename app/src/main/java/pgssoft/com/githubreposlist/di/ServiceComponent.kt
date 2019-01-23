package pgssoft.com.githubreposlist.di

import dagger.Component
import pgssoft.com.githubreposlist.services.ReposFetchingService
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class])

interface ServiceComponent {

    fun inject(service: ReposFetchingService)
}
