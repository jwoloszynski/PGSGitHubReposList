package pgssoft.com.githubreposlist.data.api

import io.reactivex.Single
import pgssoft.com.githubreposlist.data.db.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface GHApi {

    @Headers(
        "Accept: application/vnd.github.v3.full+json",
        "'User-Agent': 'PGSGHRepos'"
    )

    @GET(value = "orgs/{org}/repos?per_page=100")
    fun getOrganizationRepos(@Path("org") organizationName: String): Single<Response<List<Repository>>>


}
