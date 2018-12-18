package pgssoft.com.githubreposlist.data.api

import okhttp3.Cache
import okhttp3.OkHttpClient
import pgssoft.com.githubreposlist.PGSRepoApp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GHApiProvider {


        private val client: OkHttpClient =
            OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).cache(Cache(PGSRepoApp.app.cacheDir, 10*1024*1024))
                .build()


        fun getApi(): GHApi {
            return Retrofit.Builder().baseUrl("https://api.github.com").client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(GHApi::class.java)
        }




}