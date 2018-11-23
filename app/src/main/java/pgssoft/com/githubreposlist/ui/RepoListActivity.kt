package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.ReposFetcher
import pgssoft.com.githubreposlist.data.db.ReposDatabase
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel


class RepoListActivity : AppCompatActivity() {

    lateinit var tv: TextView
    lateinit var rv: RecyclerView
    lateinit var listModel: RepoListViewModel
    lateinit var db: ReposDatabase
    lateinit var deleteButton: Button
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val fetcher = ReposFetcher()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh)
        swipeRefreshLayout.setOnRefreshListener { onRefresh() }

        fetcher.fetchAll({ s -> })

        tv = findViewById(R.id.text)
        rv = findViewById(R.id.recyclerview)
        deleteButton = findViewById(R.id.pullButton)
        deleteButton.setOnClickListener { fetcher.clearRepoList() }

        rv.layoutManager = LinearLayoutManager(PGSRepoApp.app)
        listModel = ViewModelProviders.of(this).get(RepoListViewModel::class.java)
        db = ReposDatabase.instance!!
        listModel.getRepoCount().observe(this, Observer {
            if (it == 0) {
                tv.text = "Pull to refresh"
            } else {
                tv.text = it.toString()
            }
        })
        refreshAdapter()


    }

    private fun refreshAdapter() {
        listModel.getRepoList().observe(this, Observer {
            if (it != null) {
                var list = it.toMutableList()
                list.sortBy { it.pushed_at }
                list.reverse()
                this.rv.adapter = RepoListAdapter(list)
            }
        })
    }

    private fun onRefresh() {

        this.fetcher.fetchAll({ s -> }, true, swipeRefreshLayout)

    }


}


