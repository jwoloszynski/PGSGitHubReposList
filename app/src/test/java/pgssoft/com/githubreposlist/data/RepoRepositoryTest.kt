package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper


class RepoRepositoryTest {

    private lateinit var mApi: GHApi
    private lateinit var mDb: ReposDatabase
    private lateinit var mPrefs: PrefsHelper
    private lateinit var repoRepository: RepoRepository
    private lateinit var repoList: List<Repository>

    @Before
    fun setUp() {
        mApi = mock()
        mDb = mock()
        mPrefs = mock()
        repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(repoList)

        whenever(mDb.repoDao()).thenReturn(TestDao(repoList))

        repoRepository = RepoRepository(mApi, mDb, mPrefs)
    }

    @Test
    fun fetchAllTest_DataOk() {
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(TestCall(repoList))
        whenever(mPrefs.time).thenReturn(-1L)

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.DataOk)
    }

    @Test
    fun fetchAllTest_RequestTimeTooShort() {
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(TestCall(repoList))
        whenever(mPrefs.time).thenReturn(System.currentTimeMillis())

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.NoRefreshDueToTime)
    }

    @Test
    fun fetchAllTest_EmptyResponseMeansForbidden() {
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(TestCall(null))
        whenever(mPrefs.time).thenReturn(-1L)

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.Forbidden)
    }

    @Test
    fun fetchAllTest_ExceptionThrown() {
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(TestCall(null, true))
        whenever(mPrefs.time).thenReturn(-1L)

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue((testVal is RepoDownloadStatus.ErrorMessage) && (testVal.message == "testException"))
    }
}
