package pgssoft.com.githubreposlist.ui

interface RepoActivityInterface {

    fun onItemSelect(id: Int)

    fun showNoteDialog(id: Int, comment: String)
}
