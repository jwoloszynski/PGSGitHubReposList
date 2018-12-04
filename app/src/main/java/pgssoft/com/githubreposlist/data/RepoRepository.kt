package pgssoft.com.githubreposlist.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RepoRepository {


    val api: GHApi =
        Retrofit.Builder().baseUrl("https://api.github.com/").addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(GHApi::class.java)
    val db = ReposDatabase.getInstance(PGSRepoApp.app)


    fun fetchAll(): Observable<String> {

        var orgName = PGSRepoApp.app.getString(R.string.pgsghorgname)


        var d = api.getOrganizationRepos(orgName)
            .doOnNext {
                
                if (!it.list.isNullOrEmpty()) {
                    db.repoDao().insertAll(it.list)
                }
            }.map {
                when (it.status) {
                    0 -> "DONE"
                    1 -> "LOADING"
                    2 -> "ERROR"
                    else -> "ANOTHER ERROR"
                }
            }.doOnError { it.message.toString() }.subscribeOn(Schedulers.io())



        return d
    }


    fun getRepoList() = db.repoDao().getAll()

    fun gerRepoById(id: Int) = db.repoDao().get(id)
    fun getCount() = db.repoDao().getCount()

    fun clearRepoList(): Completable {

        return Completable.create { s ->

            db.repoDao().deleteAll()


        }.subscribeOn(Schedulers.io())

    }


}