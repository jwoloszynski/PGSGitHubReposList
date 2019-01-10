package pgssoft.com.githubreposlist.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface RepoDao {

    @Query("SELECT * FROM repository ORDER BY pushed_at DESC")
    fun getAll(): LiveData<List<Repository>>

    @Query("SELECT * FROM repository where id = :id")
    fun get(id: Int): LiveData<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repo: List<Repository>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Repository)

    @Delete
    fun delete(repo: Repository)

    @Query("SELECT count(*) FROM repository")
    fun getCount(): LiveData<Int>

    @Query("DELETE FROM repository")
    fun deleteAll()

    @Query("UPDATE repository SET comment = :comment WHERE id = :id")
    fun updateRepoComment(id: Int, comment: String)


    @Query("SELECT id,comment FROM repository WHERE id = :repoId")
    fun getCommentByRepoId(repoId: Int): Single<RepositoryComment?>

    @Query("SELECT count(*) FROM repository")
    fun getListCount(): Single<Int>
}
