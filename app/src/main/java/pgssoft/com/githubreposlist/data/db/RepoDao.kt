package pgssoft.com.githubreposlist.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface RepoDao {

    @Query("SELECT * FROM repository")
    fun getAll(): LiveData<List<Repository>>

    @Query("SELECT * FROM repository where id = :id")
    fun get(id: Int): LiveData<Repository>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repo: List<Repository>)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Repository)

    @Delete
    fun delete(repo:Repository)

    @Query("SELECT count(*) FROM repository")
    fun getCount(): LiveData<Int>

    @Query("DELETE FROM repository")
    fun deleteAll()
}