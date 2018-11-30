package pgssoft.com.githubreposlist.data

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.Single
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


    companion object {

        var instance: RepoRepository? = null
        fun getRepoInstance(): Single<RepoRepository> {

            if (instance == null) {
                instance = RepoRepository()
            }
        return Single.just(instance)
        }

    }


    val api: GHApi =
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

                    CoroutineScope(Dispatchers.IO).launch {
                        db.repoDao().insertAll(response.body()!!)
                    }
//                    Observable.just(1).doOnNext { db.repoDao().insertAll(response.body()!!) }
//                        .subscribeOn(Schedulers.io()).subscribe()


                }
            }
        })



    }

    fun getRepoList() = db.repoDao().getAll()

    fun getRepoById(id: Int) = db.repoDao().get(id)
    fun getCount() = db.repoDao().getCount()

    fun clearRepoList() {

        CoroutineScope(Dispatchers.IO).launch {
            db.repoDao().deleteAll()
        }

//        Observable.just(1).doOnNext { db.repoDao().deleteAll() }
//            .subscribeOn(Schedulers.io()).subscribe()

    }


}
