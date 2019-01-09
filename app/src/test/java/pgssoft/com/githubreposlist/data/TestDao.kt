package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import pgssoft.com.githubreposlist.data.db.RepoDao
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.data.db.RepositoryComment

class TestDao(private val list: List<Repository>) : RepoDao {

    override fun getAll(): LiveData<List<Repository>> {
        val livedata: LiveData<List<Repository>>

        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(list)
        livedata = mutableLiveData

        return livedata
    }

    override fun get(id: Int): LiveData<Repository> {
        val livedata: LiveData<Repository>

        val mutableLiveData = MutableLiveData<Repository>()
        mutableLiveData.postValue(list[id])
        livedata = mutableLiveData

        return livedata
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
        val livedata: LiveData<Int>

        val mutableLiveData = MutableLiveData<Int>()
        mutableLiveData.postValue(list.size)
        livedata = mutableLiveData

        return livedata
    }

    override fun deleteAll() {
        return
    }

    override fun updateRepoComment(id: Int, comment: String) {
        return
    }

    override fun getCommentByRepoId(repoId: Int): RepositoryComment? {

        return RepositoryComment(repoId, repoId.toString())

    }

    override fun getListCount(): Int {
        return list.size
    }
}
