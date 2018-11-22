package pgssoft.com.githubreposlist.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Repository::class), version = 1)
abstract class ReposDatabase: RoomDatabase() {
abstract fun repoDao() : RepoDao




}