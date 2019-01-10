package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper
import retrofit2.Response


class RepoRepositoryTest {

    private lateinit var mApi: GHApi
    private lateinit var mDb: ReposDatabase
    private lateinit var mPrefs: PrefsHelper
    private lateinit var repoRepository: RepoRepository
    private lateinit var repoList: List<Repository>
    private lateinit var response: Response<List<Repository>>
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
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(Single.just(response))
        whenever(mPrefs.time).thenReturn(-1L)

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal.blockingGet() == RepoDownloadStatus.DataOk)
    }

    @Test
    fun fetchAllTest_RequestTimeTooShort() {
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(Single.just(response))
        whenever(mPrefs.time).thenReturn(System.currentTimeMillis())

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal.blockingGet() == RepoDownloadStatus.NoRefreshDueToTime)
    }

    @Test
    fun fetchAllTest_EmptyResponseMeansForbidden() {

        val responseEmpty = retrofit2.Response.error<List<Repository>>(
            403,
            ResponseBody.create(

                MediaType.parse("application/json"),
                "{\"error\":[\"forbidden\"]}"
            )
        )
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(Single.just(responseEmpty))
        whenever(mPrefs.time).thenReturn(-1L)

        val testVal = repoRepository.fetchAll()
        Assert.assertTrue(testVal.blockingGet() == RepoDownloadStatus.Forbidden)
    }

    @Test
    fun fetchAllTest_AnotherError() {
        val responseEmpty = retrofit2.Response.error<List<Repository>>(
            418,
            ResponseBody.create(

                MediaType.parse("application/json"),
                "{\"easteregg\":[\"I'm a teapot\"]}"
            )
        )
        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString())).thenReturn(Single.just(responseEmpty))
        whenever(mPrefs.time).thenReturn(-1L)

        val testResult = repoRepository.fetchAll()
        val testVal = testResult.blockingGet()
        Assert.assertTrue((testVal is RepoDownloadStatus.ErrorMessage))
    }

}
