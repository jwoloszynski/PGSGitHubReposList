package pgssoft.com.githubreposlist.data.api

import android.util.Log
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.db.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class RepoListResponse : Callback<List<Repository>> {

    var status: RepoDownloadStatus= RepoDownloadStatus.DataOk
    var list: List<Repository> = listOf()

    override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
        status = RepoDownloadStatus.ErrorMessage(t.message.toString())
    }

    override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {

        try {


            if (response.body().isNullOrEmpty()) {
                status = RepoDownloadStatus.Forbidden
            } else {
                list = response.body()!!
                status = RepoDownloadStatus.DataOk
            }
        }
        catch (e:Exception){
            Log.d("LISTRESPONSE","Cached on list response")
        }
    }

}