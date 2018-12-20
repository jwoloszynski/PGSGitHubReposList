package pgssoft.com.githubreposlist.data

import okhttp3.Request
import pgssoft.com.githubreposlist.data.db.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestCall(private val list: List<Repository>) : Call<List<Repository>> {
    override fun execute(): Response<List<Repository>> {
        return retrofit2.Response.success(list)
    }

    override fun enqueue(callback: Callback<List<Repository>>) {
        return enqueue(callback)
    }

    override fun isExecuted(): Boolean {
        return true
    }

    override fun clone(): Call<List<Repository>> {
        return this
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun cancel() {
        return cancel()
    }

    override fun request(): Request {
        return request()
    }
}