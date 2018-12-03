package pgssoft.com.githubreposlist.ui

interface RepoActivityInterface {

    fun onItemSelect(id: Int)

    fun showError(message: String)

    fun showNoteDialog(id: Int, comment: String)
}
