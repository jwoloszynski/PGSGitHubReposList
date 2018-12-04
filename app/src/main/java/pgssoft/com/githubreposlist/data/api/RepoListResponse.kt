package pgssoft.com.githubreposlist.data.api

import pgssoft.com.githubreposlist.data.db.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RepoListResponse : Callback<List<Repository>> {

    var status: Int = 0
    var list: List<Repository> = listOf()

    override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
        status = 2
    }

    override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {

        if(response.body().isNullOrEmpty()){
            status = 3
        }
        else {
            list = response.body()!!
        }
    }
}