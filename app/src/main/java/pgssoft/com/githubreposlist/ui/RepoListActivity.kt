package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_repo_list.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel


class RepoListActivity : AppCompatActivity() {

    lateinit var listModel: RepoListViewModel

    private val repoListAdapter: RepoListAdapter = RepoListAdapter(listOf())
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        recyclerView.layoutManager = LinearLayoutManager(PGSRepoApp.app)
        swipeToRefresh.setOnRefreshListener { onRefresh() }
        deleteButton.setOnClickListener { listModel.clearRepoList() }

        recyclerView.adapter = repoListAdapter
        listModel = ViewModelProviders.of(this).get(RepoListViewModel::class.java)
        listModel.getRepoCountText().observe(this, Observer { textRepoCount.text = it }
        )

        refreshAdapter()
        listenErrors()

    }

    private fun onRefresh() {
        listModel.onRefresh(repoListAdapter.itemCount)
        recyclerView.layoutManager?.smoothScrollToPosition(recyclerView,null,0)

    }

    private fun listenErrors() {

        listModel.repoListErrorText.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                AlertDialog.Builder(this).setTitle(R.string.error).setMessage(it).setPositiveButton("OK")
                { d, i ->
                    d.dismiss()
                }
                    .create().show()
            }

            swipeToRefresh.isRefreshing = false
        })

    }

    private fun refreshAdapter() {
        listModel.getRepoList().observe(this, Observer {

            repoListAdapter.setData(it!!)
        })
    }
}


