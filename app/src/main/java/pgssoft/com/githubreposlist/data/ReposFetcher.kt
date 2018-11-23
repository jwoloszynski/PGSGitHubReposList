package pgssoft.com.githubreposlist.data


import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import io.reactivex.Observable
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


class ReposFetcher {

    val api =
        Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create()).build()
            .create(GHApi::class.java)
    val db = ReposDatabase.getInstance(PGSRepoApp.app)

    fun fetchAll(
        callback: (List<Repository>) -> Unit,
        forced: Boolean = false,
        refreshLayout: SwipeRefreshLayout? = null
    ) {
        var orgName = PGSRepoApp.app.getString(R.string.pgsghorgname)



        if (forced) {
            api.getOrganizationRepos(orgName).enqueue(object : Callback<List<Repository>> {
                override fun onFailure(call: Call<List<Repository>>, t: Throwable) {

                    throw IllegalStateException()
                }

                override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {

                    if (response.body() == null) {
                        Log.d("DEBUG", response.raw().message())
                    } else {
                        callback(response.body()!!)
                        Observable.fromIterable(response.body())
                            .doOnNext { n -> db.repoDao().insert(n) }
                            .subscribe()
                        if (refreshLayout != null)
                            refreshLayout.isRefreshing = false
                    }
                }
            })
        }
    }

    fun clearRepoList() {
        Observable.just(1).doOnNext { d -> db.repoDao().deleteAll() }.subscribe()

    }

}

