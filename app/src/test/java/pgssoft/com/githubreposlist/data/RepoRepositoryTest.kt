package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper


class RepoRepositoryTest {


    private lateinit var mApi: GHApi
    private lateinit var mDb: ReposDatabase
    private lateinit var mPrefs: PrefsHelper
    private lateinit var repoRepository: RepoRepository

    @Before
    fun setUp() {
        mApi = mock(GHApi::class.java)
        mDb = mock(ReposDatabase::class.java)
        mPrefs = mock(PrefsHelper::class.java)


        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(repoList)

        doReturn(TestCall(repoList)).whenever(mApi).getOrganizationRepos(ArgumentMatchers.anyString())
        doReturn(TestDao(repoList)).whenever(mDb).repoDao()
        repoRepository = RepoRepository(mApi, mDb, mPrefs)

    }

    @Test
    fun fetchAllTest_DataOk() {

        doReturn(-1L).whenever(mPrefs).time

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.DataOk)
    }

    @Test
    fun fetchAllTest_RequestTimeTooShort() {

        doReturn(System.currentTimeMillis()).whenever(mPrefs).time

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.NoRefreshDueToTime)
    }
}




