package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.applicationContext
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import pgssoft.com.githubreposlist.app_module
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper


class RepoRepositoryTest {



    private val mApi = mock(GHApi::class.java)

    private val mDb = mock(ReposDatabase::class.java)

    private val mPrefs = mock(PrefsHelper::class.java)
    private lateinit var repoRepository: RepoRepository

    @Before
    fun setUp() {

        startKoin(
            listOf(
               module {
                   single { mApi }
                   single { mDb }
                   single { mPrefs }
               }

            )
        )
        repoRepository = RepoRepository()
        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(repoList)
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(TestCall(repoList))
        whenever(mDb.repoDao()).thenReturn(TestDao(repoList))

    }

    @Test
    fun fetchAllTest_DataOk() {

        doReturn(-1L).`when`(mPrefs).time
        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.DataOk)

    }

    @Test
    fun fetchAllTest_RequestTimeTooShort() {

        doReturn(System.currentTimeMillis()).`when`(mPrefs).time
        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal == RepoDownloadStatus.NoRefreshDueToTime)

    }

    @After
    fun onFinish() {
        stopKoin()
    }
}




