package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
        setContentView(R.layout.activity_repolist)
        setSupportActionBar(findViewById(R.id.tool_bar))
        PGSRepoApp.app.appComponent.inject(this)
        repoViewModel = ViewModelProviders.of(this, repoVMFactory).get(RepoViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    replace(R.id.detail, RepoDetailFragment())
                }
                replace(R.id.list, RepoListFragment())
                commit()
            }
        } else {
            if (supportFragmentManager.findFragmentById(R.id.list) is RepoDetailFragment) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.list, RepoListFragment())
                    replace(R.id.detail, RepoDetailFragment())
                    commit()
                }
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_repolist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val f = supportFragmentManager.findFragmentById(R.id.list)

        return when (item?.itemId) {

            R.id.action_settings -> {
                Toast.makeText(this, "option1", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_refresh -> {
                (f as RepoListFragment).onRefresh()
                true
            }
            R.id.action_clearList -> {

                (f as RepoListFragment).clearRepoList()

                false
            }


            else -> super.onOptionsItemSelected(item)
        }
    }
}
