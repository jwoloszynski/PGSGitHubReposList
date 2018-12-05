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
import pgssoft.com.githubreposlist.data.RepoStatus
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel

class RepoListActivity : AppCompatActivity(), RepoActivityInterface {

    lateinit var repoViewModel: RepoViewModel
    lateinit var listModel: RepoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repolist)
        val fragmentList = RepoListFragment()

        supportFragmentManager.beginTransaction().apply {

            add(R.id.list, fragmentList)
            commit()
        }

        val fragmentDetail = RepoDetailFragment()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            supportFragmentManager.beginTransaction().apply {
                add(R.id.detail, fragmentDetail)
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
        val detailFragment = RepoDetailFragment()
        detailFragment.arguments = args


        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.detail, detailFragment)
                commit()

            }
        } else {

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.list, detailFragment)
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

        listModel.statusLiveData.observe(this, EventObserver<RepoStatus> {

            when (it) {
                is RepoStatus.DataOk -> swipeToRefresh.isRefreshing = false
                is RepoStatus.Error -> {
                    showError(it.message)
                    swipeToRefresh.isRefreshing = false
                }

            }


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