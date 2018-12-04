package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RepoRepository {


    private val api: GHApi =
        Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create()).build()
            .create(GHApi::class.java)
    val db = ReposDatabase.getInstance(PGSRepoApp.app)


    fun fetchAll(error: MutableLiveData<String>) {
        var orgName = PGSRepoApp.app.getString(R.string.pgsghorgname)

        api.getOrganizationRepos(orgName).enqueue(object : Callback<List<Repository>> {
            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {

                error.value = "Connection Error"
            }

            override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {

                if (response.body() == null) {
                    Log.d("DEBUG", response.raw().message())
                    error.value = response.raw().message()
                } else {
                    error.value = ""

                    var repoList = response.body()!!

                    CoroutineScope(Dispatchers.IO).launch {

                        repoList = repoList.sortedByDescending { it.pushed_at }
                        for (repo in repoList) {

                            getCommentByRepoId(repo.id)?.subscribe {
                                repo.comment = it?.comment?:""
                            }

                        }

                        db.repoDao().insertAll(repoList)
                    }


                }
            }
        })


    }

    fun getRepoList() = db.repoDao().getAll()

    fun getRepoById(id: Int) = db.repoDao().get(id)
    fun getCount() = db.repoDao().getCount()
    fun getCommentByRepoId(repoId: Int) = db.repoDao().getCommentByRepoId(repoId)

    fun clearRepoList() {

        CoroutineScope(Dispatchers.IO).launch {
            db.repoDao().deleteAll()
        }


    }


    fun updateRepo(id: Int, comment: String) {

        CoroutineScope(Dispatchers.IO).launch {
            db.repoDao().updateRepoComment(id, comment)
        }
    }


}
