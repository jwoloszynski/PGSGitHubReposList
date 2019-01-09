package pgssoft.com.githubreposlist.data

sealed class RepoDownloadStatus {

    class ErrorMessage(val message: String = "") : RepoDownloadStatus()
    object Forbidden : RepoDownloadStatus()
    object DataOk : RepoDownloadStatus()
    object NoRefreshDueToTime : RepoDownloadStatus()
    object ErrorNoConnection : RepoDownloadStatus()

}



