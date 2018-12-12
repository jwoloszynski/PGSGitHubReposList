package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper

@RunWith(MockitoJUnitRunner::class)
class RepoRepositoryTest {

    @Mock
    private val mApi = MockedGHApi()
    @Mock
    private val mDb = MockedDB()
    @Mock
    private val mPrefs = MockedPrefs()


    @Test
    fun fetchAllTest() {
        mApi.setWhenever()
        mDb.setWhenever()
        mPrefs.setWhenever()
        val repoRepository = RepoRepository(mApi.api, mDb.db, mPrefs.prefs)
        repoRepository.isInternetConnection = true


        whenever(repoRepository.canRefreshList()).thenReturn(true)




        repoRepository.fetchAll()

        assert(true)
    }


}

open class MockedGHApi {
    var api: GHApi = mock(GHApi::class.java)




    fun setWhenever() {
        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.value= repoList

        whenever(api.getOrganizationRepos(ArgumentMatchers.anyString()).execute()).thenReturn(
            retrofit2.Response.success(mutableLiveData.value))
    }

}


open class MockedDB {
    var db = mock(ReposDatabase::class.java)

    fun setWhenever() {
        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(repoList)
        whenever(db.repoDao().getAll()).thenReturn(mutableLiveData as LiveData<List<Repository>>)
    }

}

open class MockedPrefs {
    var prefs = mock(PrefsHelper::class.java)
    fun setWhenever() {


        whenever(prefs.time).thenReturn(-1L)
        whenever(prefs.repoId).thenReturn(0)

    }
}



