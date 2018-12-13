package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(MockitoJUnitRunner::class)
class RepoRepositoryTest {

    @Mock
    private val mApi = /* getMockApi() */ mock(GHApi::class.java)
    @Mock
    private val mDb = mock(ReposDatabase::class.java)
    @Mock
    private val mPrefs = mock(PrefsHelper::class.java)

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun fetchAllTest() {

        val repoRepository = RepoRepository(mApi, mDb, mPrefs)
        repoRepository.isInternetConnection = true


        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()
        mutableLiveData.postValue(repoList)

        whenever(mDb.repoDao().getAll()).thenReturn(mutableLiveData as LiveData<List<Repository>>)
        whenever(mDb.repoDao().getListCount()).thenReturn(3)
        whenever(mPrefs.time).thenReturn(-1L)
        whenever(mPrefs.repoId).thenReturn(0)

        whenever(mApi.getOrganizationRepos(ArgumentMatchers.anyString()).execute()).thenReturn(
            retrofit2.Response.success(mutableLiveData.value))

        repoRepository.fetchAll()

    }

    private fun getMockApi():GHApi {

            return Retrofit.Builder().baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create()).build().create(GHApi::class.java)

        }


}


