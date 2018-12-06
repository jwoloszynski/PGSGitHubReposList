package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.dialog_note.view.*
import kotlinx.android.synthetic.main.fragment_repo_list.*
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.EventObserver
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel

class RepoListActivity : AppCompatActivity(), RepoActivityInterface {

    lateinit var repoViewModel: RepoViewModel
    lateinit var listModel: RepoListViewModel

    private val detailFragment = RepoDetailFragment()
    private val fragmentList = RepoListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repolist)

        supportFragmentManager.beginTransaction().apply {

            add(R.id.list, fragmentList)
            commit()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {


            supportFragmentManager.beginTransaction().apply {
                add(R.id.detail, detailFragment)
                commit()
            }
        }


        repoViewModel = ViewModelProviders.of(this).get(RepoViewModel::class.java)
        listModel = ViewModelProviders.of(this).get(RepoListViewModel::class.java)

        listenStatus()
    }

    override fun onItemSelect(id: Int) {
        val args = Bundle()
        args.putInt("id", id)
        detailFragment.arguments = args
        repoViewModel.repository = repoViewModel.getRepoById(id)


        if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.list, detailFragment)
                addToBackStack(null)
                commit()

            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.detail, detailFragment)
                addToBackStack(null)
                commit()

            }
        }

    }


    override fun showNoteDialog(id: Int, comment: String) {
        val v: View = layoutInflater.inflate(R.layout.dialog_note, null)

        v.comment.setText(comment)

        val title = if (comment.isEmpty()) "Add note" else "Edit note"


        AlertDialog.Builder(this).setTitle(title).setView(v)
            .setPositiveButton("OK")
            { _, _ ->
                repoViewModel.update(id, v.comment.text.toString())
            }
            .create().show()
    }


    private fun listenStatus() {

        listModel.statusLiveData.observe(this, EventObserver {

            when (it) {
                is RepoDownloadStatus.DataOk -> {
                }
                is RepoDownloadStatus.Error -> {
                    showError(it.message)

                }
            }
            swipeToRefresh.isRefreshing = false

        })

    }

    private fun showError(message: String) {

        AlertDialog.Builder(this).setTitle(R.string.error).setMessage(message)
            .setPositiveButton("OK")
            { d, _ ->
                d.dismiss()
            }
            .create().show()
    }


}