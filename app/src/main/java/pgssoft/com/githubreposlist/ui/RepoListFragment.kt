package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repo_list.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.EventObserver
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel


class RepoListFragment : Fragment() {

    private val repoViewModel: RepoViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RepoViewModel::class.java)
    }


    private var repoListAdapter: RepoListAdapter = RepoListAdapter(listOf())


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        repoListAdapter = RepoListAdapter(listOf(), activity as RepoListActivity)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(PGSRepoApp.app)
        swipeToRefresh.setOnRefreshListener { onRefresh() }
        deleteButton.setOnClickListener { repoViewModel.clearRepoList() }

        recyclerView.adapter = repoListAdapter
        repoViewModel.getRepoCountText().observe(this, Observer { textRepoCount.text = it }
        )

        refreshAdapter()
    }

    private fun onRefresh() {

        swipeToRefresh.isRefreshing = true
        repoViewModel.onRefresh()
        repoViewModel.refreshState.observe(this, EventObserver {

            when (it) {
                is RepoDownloadStatus.DataOk -> {
                }
                is RepoDownloadStatus.ErrorMessage -> {
                    showError(it.message)
                }
                is RepoDownloadStatus.Forbidden -> {
                    showError(this.getString(R.string.rate_limit_exceeded))
                }

            }
            swipeToRefresh.isRefreshing = false

        })

    }


    private fun refreshAdapter() {
        repoViewModel.getRepoList().observe(this, Observer {

            repoListAdapter.setData(it!!)

        })

    }

    private fun showError(message: String) {

        AlertDialog.Builder(activity!!).setTitle(R.string.error).setMessage(message)
            .setPositiveButton(R.string.ok)
            { d, _ ->
                d.dismiss()
            }
            .create().show()
    }

}


