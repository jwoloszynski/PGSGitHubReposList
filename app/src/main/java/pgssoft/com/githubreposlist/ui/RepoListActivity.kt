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

class RepoListActivity : AppCompatActivity(), RepoActivityInterface {

    lateinit var repoViewModel: RepoViewModel

    private val detailFragment = RepoDetailFragment()
    private val listFragment = RepoListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repolist)

        supportFragmentManager.beginTransaction().apply {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                add(R.id.detail, detailFragment)
            }
            add(R.id.list, listFragment)
            commit()
        }

        repoViewModel = ViewModelProviders.of(this).get(RepoViewModel::class.java)

    }

    override fun onItemSelect(id: Int) {
        val args = Bundle()
        args.putInt("id", id)
        detailFragment.arguments = args
        repoViewModel.getRepoById(id)

        supportFragmentManager.beginTransaction().apply {
            if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                replace(R.id.list, detailFragment)
                addToBackStack(null)

            } else {
                detach(detailFragment)
                attach(detailFragment)
            }
            commit()
        }
    }


    override fun showNoteDialog(id: Int, comment: String) {
        val v = View.inflate(this, R.layout.dialog_note, null).also { it.comment.setText(comment) }

        val title =
            if (comment.isEmpty()) PGSRepoApp.app.getString(R.string.add_note) else this.getString(R.string.edit_note)

        AlertDialog.Builder(this).setTitle(title).setView(v)
            .setPositiveButton(PGSRepoApp.app.getText(R.string.ok))
            { _, _ ->
                repoViewModel.update(id, v.comment.text.toString())
            }
            .create().show()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onItemSelect(id)
        }
    }


}