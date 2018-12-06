package pgssoft.com.githubreposlist.data

sealed class RepoDownloadStatus {

    class Error (val message: String): RepoDownloadStatus()
    object DataOk : RepoDownloadStatus()

}



