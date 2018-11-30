package pgssoft.com.githubreposlist.data

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.api.GHApi
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.data.db.Repository
import retrofit2.Response
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
        var errorString = ""


        api.getOrganizationRepos(orgName).subscribeOn(Schedulers.io()).subscribe(

            object : Observer<Response<List<Repository>>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(response: Response<List<Repository>>) {

                    if (response.isSuccessful) {
                        if(response.body() == null){

                            Log.d("DEBUG", response.raw().message())
                            errorString = response.raw().message()
                        }
                        else{
                        db.repoDao().insertAll(response.body()!!)
                        }

                    } else {
                        errorString = "Connection Error"
                    }

                }

                override fun onError(e: Throwable) {

                }
            })

        return Observable.just(errorString)
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