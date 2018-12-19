package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import pgssoft.com.githubreposlist.data.db.RepoDao
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.data.db.RepositoryComment

class TestDao(private val list: List<Repository>) : RepoDao {

    override fun getAll(): LiveData<List<Repository>> {
        var livedata: LiveData<List<Repository>>

        var mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(list)
        livedata = mutableLiveData

        return livedata
    }

    override fun get(id: Int): LiveData<Repository> {
        var livedata: LiveData<Repository>

        var mutableLiveData = MutableLiveData<Repository>()
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
        var livedata: LiveData<Int>

        var mutableLiveData = MutableLiveData<Int>()
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

        val rc = RepositoryComment(repoId, repoId.toString())
        return rc

    }

    override fun getListCount(): Int {
        return list.size
    }
}
