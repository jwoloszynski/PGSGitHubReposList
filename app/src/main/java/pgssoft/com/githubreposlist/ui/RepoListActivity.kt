package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
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
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel


class RepoListActivity : AppCompatActivity() {

    lateinit var tv: TextView
    lateinit var rv: RecyclerView
    lateinit var listModel: RepoListViewModel
    lateinit var db: ReposDatabase
    lateinit var deleteButton: Button
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var prefs: PrefsHelper
    private val fetcher = ReposFetcher()
    private var list: MutableList<Repository> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = ReposDatabase.instance!!
        setContentView(R.layout.activity_repo_list)
        tv = findViewById(R.id.text)
        rv = findViewById(R.id.recyclerview)
        rv.layoutManager = LinearLayoutManager(PGSRepoApp.app)
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh)
        swipeRefreshLayout.setOnRefreshListener { onRefresh() }

        deleteButton = findViewById(R.id.pullButton)
        deleteButton.setOnClickListener { fetcher.clearRepoList() }


        listModel = ViewModelProviders.of(this).get(RepoListViewModel::class.java)
        listModel.getRepoCount().observe(this, Observer {
            if (it == 0) {
                tv.text = "Pull to refresh"
            } else {
                tv.text = it.toString()
            }
        })

        refreshAdapter()

    }


    private fun onRefresh() {
        prefs = PrefsHelper(PGSRepoApp.app)
        val timeRefreshed = prefs.time
        val timeBetween = System.currentTimeMillis() - timeRefreshed
        if ((timeRefreshed == -1L) or (timeBetween > (1 * 60 * 1000)) or list.isEmpty()) {
            fetcher.fetchAll { response,error -> getResponse(response,error) }
            prefs.time = System.currentTimeMillis()
        } else {
            swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun getResponse(response: List<Repository>, error:String? = null) {
        tv.text = error?: ""
        swipeRefreshLayout.isRefreshing = false

    }

    private fun refreshAdapter() {
        listModel.getRepoList().observe(this, Observer {
            if (it != null) {
                list = it.toMutableList()
                list.sortByDescending { it.pushed_at }
                this.rv.adapter = RepoListAdapter(list)
            }
        })
    }
}


