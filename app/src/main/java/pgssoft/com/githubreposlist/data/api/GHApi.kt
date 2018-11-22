package pgssoft.com.githubreposlist.data.api

import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GHApi {

    @GET(value = "orgs/{oname}/repos")
    fun getOrganizationRepos (@Path("oname") organizationName: String) : Call

}