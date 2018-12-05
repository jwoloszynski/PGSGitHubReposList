package pgssoft.com.githubreposlist.data

sealed class RepoStatus {

    class Error (val message: String): RepoStatus()
    class DataOk (): RepoStatus()

}
