package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.dialog_note.view.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel
import pgssoft.com.githubreposlist.viewmodels.RepoViewModelFactory
import javax.inject.Inject

/**
 * An activity containing and managing displayed fragments
 */

class ReposActivity : AppCompatActivity() {

    @Inject
    lateinit var repoVMFactory: RepoViewModelFactory
    private lateinit var repoViewModel: RepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PGSRepoApp.app.appComponent.inject(this)
        repoViewModel = ViewModelProviders.of(this, repoVMFactory).get(RepoViewModel::class.java)
        setContentView(R.layout.activity_repolist)

        supportFragmentManager.beginTransaction().apply {

            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && savedInstanceState == null) {
                replace(R.id.detail, RepoDetailFragment())
            }
            replace(R.id.list, RepoListFragment())
            commit()
        }
    }

    fun showDetail() {
        supportFragmentManager.beginTransaction().apply {
            if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                replace(R.id.list, RepoDetailFragment())
                addToBackStack(null)
            } else {
                replace(R.id.detail, RepoDetailFragment())
            }
            commit()
        }
    }

    fun showNoteDialog(id: Int, comment: String) {
        val v = View.inflate(
            this,
            R.layout.dialog_note, null
        )
            .also {
                it.comment.setText(comment)
            }

        val title =
            if (comment.isEmpty()) getString(R.string.add_note) else this.getString(R.string.edit_note)

        AlertDialog.Builder(this).setTitle(title).setView(v)
            .setPositiveButton(getText(R.string.ok))
            { _, _ ->
                repoViewModel.update(id, v.comment.text.toString())
            }
            .create().show()
    }
}