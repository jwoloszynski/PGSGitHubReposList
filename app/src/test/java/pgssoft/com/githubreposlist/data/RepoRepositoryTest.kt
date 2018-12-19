package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper


@RunWith(MockitoJUnitRunner::class)
class RepoRepositoryTest {


    @Mock
    private val mApi = mock(GHApi::class.java, RETURNS_DEEP_STUBS)
    @Mock
    private val mDb = mock(ReposDatabase::class.java)
    @Mock
    private val mPrefs = mock(PrefsHelper::class.java)

    @Test
    fun fetchAllTest() {


        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(repoList)
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(TestCall(repoList))
        whenever(mDb.repoDao()).thenReturn(TestDao(repoList))


        val repoRepository = RepoRepository(mApi, mDb, mPrefs)
        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.DataOk)
    }
}




