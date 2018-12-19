package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.mock
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper


class RepoRepositoryTest {


    private val mApi = mock(GHApi::class.java, RETURNS_DEEP_STUBS)
    private val mDb = mock(ReposDatabase::class.java)
    private val mPrefs = mock(PrefsHelper::class.java)

    @Test
    fun fetchAllTest() {


        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(repoList)

        doReturn(TestCall(repoList)).whenever(mApi).getOrganizationRepos(ArgumentMatchers.anyString())
        doReturn(TestDao(repoList)).whenever(mDb).repoDao()
        doReturn(-1L).whenever(mPrefs).time

        val repoRepository = RepoRepository(mApi, mDb, mPrefs)
        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.DataOk)
    }
}




