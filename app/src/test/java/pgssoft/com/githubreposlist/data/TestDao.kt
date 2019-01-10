package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Single
import pgssoft.com.githubreposlist.data.db.RepoDao
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.data.db.RepositoryComment

class TestDao(private val list: List<Repository>) : RepoDao {

    override fun getAll(): LiveData<List<Repository>> {
        val liveData: LiveData<List<Repository>>

        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(list)
        liveData = mutableLiveData

        return liveData
    }

    override fun get(id: Int): LiveData<Repository> {
        val liveData: LiveData<Repository>

        val mutableLiveData = MutableLiveData<Repository>()
        mutableLiveData.postValue(list[id])
        liveData = mutableLiveData

        return liveData
    }

    override fun insertAll(repo: List<Repository>) {
        return
    }

    override fun insert(repo: Repository) {
        return
    }

    override fun delete(repo: Repository) {
        return
    }

    override fun getCount(): LiveData<Int> {
        val liveData: LiveData<Int>

        val mutableLiveData = MutableLiveData<Int>()
        mutableLiveData.postValue(list.size)
        liveData = mutableLiveData

        return liveData
    }

    override fun deleteAll() {
        return
    }

    override fun updateRepoComment(id: Int, comment: String) {
        return
    }

    override fun getCommentByRepoId(repoId: Int): Single<RepositoryComment?> {


        return Single.just(RepositoryComment(repoId, repoId.toString()))


    }

    override fun getListCount(): Single<Int> {
        return Single.just(list.size)
    }
}
