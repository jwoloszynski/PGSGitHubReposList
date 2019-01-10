package pgssoft.com.githubreposlist.data.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context


@Database(entities = [Repository::class], version = 2)
abstract class ReposDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

    companion object {

        var instance: ReposDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `repository` ADD COLUMN `comment` TEXT")
            }
        }

        fun getInstance(context: Context): ReposDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, ReposDatabase::class.java, "pgs-gh-repos")
                    .addMigrations(MIGRATION_1_2).build()
            }
            return instance!!
        }
    }

}
