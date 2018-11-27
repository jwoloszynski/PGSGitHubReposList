package pgssoft.com.githubreposlist.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Repository::class], version = 1)
abstract class ReposDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

    companion object {

        var instance: ReposDatabase? = null
        fun getInstance(context: Context): ReposDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, ReposDatabase::class.java, "pgs-gh-repos")
                    .build()
            }
            return instance!!
        }

    }


}