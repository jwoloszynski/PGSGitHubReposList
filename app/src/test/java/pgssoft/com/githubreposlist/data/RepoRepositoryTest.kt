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
    private val api = mock(GHApi::class.java)
    @Mock
    private val db = mock(ReposDatabase::class.java)
    @Mock
    private val prefs = mock(PrefsHelper::class.java)


    @Test
    fun fetchAllTest() {


        val repoList = getTestReposList()
        val mutableLiveData = MutableLiveData<List<Repository>>()

        mutableLiveData.postValue(repoList)
        val repoRepository = RepoRepository(api, db, prefs)
        repoRepository.isInternetConnection = true
        whenever(repoRepository.canRefreshList()).thenReturn(true)

        whenever(api.getOrganizationRepos(ArgumentMatchers.anyString()).execute()).thenReturn(
            retrofit2.Response.success(mutableLiveData.value)
        )
        whenever(db.repoDao().getAll()).thenReturn(mutableLiveData as LiveData<List<Repository>>)


        repoRepository.fetchAll()

        assert(true)
    }


}
