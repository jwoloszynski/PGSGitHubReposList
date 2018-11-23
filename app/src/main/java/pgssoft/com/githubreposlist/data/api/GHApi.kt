package pgssoft.com.githubreposlist.data.api

import pgssoft.com.githubreposlist.data.db.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GHApi {

    @Headers(
        "Accept: application/vnd.github.v3.full+json",
        "'User-Agent': 'PGSGHRepos'"
)

    @GET(value = "orgs/{org}/repos?per_page=100")
    fun getOrganizationRepos (@Path("org") organizationName: String) : Call<List<Repository>>


}
