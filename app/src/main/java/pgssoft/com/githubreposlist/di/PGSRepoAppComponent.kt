package pgssoft.com.githubreposlist.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PGSRepoModule::class])
interface PGSRepoAppComponent {



}