package pgssoft.com.githubreposlist.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_note.view.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.services.ReposFetchingService
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
    var menu: Menu? = null


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
                val frag = supportFragmentManager.findFragmentById(R.id.detail)
                supportFragmentManager.beginTransaction().apply {

                    replace(R.id.list, RepoListFragment())
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                        replace(R.id.detail, RepoDetailFragment())
                    } else if (frag != null) {
                        remove(frag)
                    }
                    commit()
                }
            }
        }

        setRepeatingFetching()
    }

    private fun setRepeatingFetching() {

        val i = Intent(this, ReposFetchingService::class.java)
        val pIntent = PendingIntent.getService(this,3434,i,PendingIntent.FLAG_UPDATE_CURRENT)
        var alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating( AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
            15*1000, pIntent)

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

        AlertDialog.Builder(this, R.style.PGSAppAlertDialog).setTitle(title).setView(v)
            .setPositiveButton(getText(R.string.ok))
            { _, _ ->
                repoViewModel.getRepoById(id)
                repoViewModel.updateRepoComment(id, v.comment.text.toString())
            }
            .create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_repolist, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            this.menu = menu

            when (supportFragmentManager.findFragmentById(R.id.list)) {
                is RepoDetailFragment -> {
                    menu.findItem(R.id.action_like).isVisible = true
                    menu.findItem(R.id.action_refresh).isVisible = false
                    menu.findItem(R.id.action_clearList).isVisible = false
                }
                is RepoListFragment -> {

                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        menu.findItem(R.id.action_like).isVisible = false

                    }
                    menu.findItem(R.id.action_refresh).isVisible = true
                    menu.findItem(R.id.action_clearList).isVisible = true
                }
            }

            val frag = supportFragmentManager.findFragmentById(R.id.detail)
            when (frag) {
                is RepoDetailFragment -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE || frag.isVisible) {
                        if (repoViewModel.isSelected) {

                            with(menu.findItem(R.id.action_like)) {
                                isVisible = true
                                repoViewModel.selected.observe(this@ReposActivity, Observer {
                                    icon =
                                            if (it?.liked == true) getDrawable(android.R.drawable.ic_input_delete) else getDrawable(
                                                android.R.drawable.ic_input_add
                                            )
                                })
                            }
                        }
                    }
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val frag = supportFragmentManager.findFragmentById(R.id.list)
        val listFragment = if (frag is RepoListFragment) frag else null

        return when (item?.itemId) {

            R.id.action_settings -> {
                Toast.makeText(this, "option1", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_refresh -> {
                listFragment?.onRefresh()
                true
            }
            R.id.action_clearList -> {

                listFragment?.clearRepoList()

                false
            }
            R.id.action_like -> {

                item.icon =
                        if (repoViewModel.changeSelectedLike())
                            this.getDrawable(android.R.drawable.ic_input_add)
                        else
                            this.getDrawable(android.R.drawable.ic_input_delete)
                false
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        invalidateOptionsMenu()

        super.onBackPressed()
    }
}
