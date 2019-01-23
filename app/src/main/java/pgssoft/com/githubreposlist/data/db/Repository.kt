package pgssoft.com.githubreposlist.data.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Repository(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "language") var language: String?,
    @ColumnInfo(name = "updated_at") var updated_at: String?,
    @ColumnInfo(name = "pushed_at") var pushed_at: String?,
    @ColumnInfo(name = "comment") var comment: String?,
    @ColumnInfo(name = "liked") var liked: Boolean?


)
