package pgssoft.com.githubreposlist.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface RepoDao {

    @Query("SELECT * FROM repository")
    fun getAll(): List<Repository>

    @Insert
    fun insertAll(vararg repo: Repository)

    @Delete
    fun delete(repo:Repository)

    @Delete
    fun deleteAll()
}