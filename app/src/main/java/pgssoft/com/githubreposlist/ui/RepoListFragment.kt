package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import pgssoft.com.githubreposlist.viewmodels.RepoViewModelFactory
import javax.inject.Inject


class RepoListFragment : Fragment() {

    @Inject
    lateinit var repoVMFactory: RepoViewModelFactory

    private lateinit var repoViewModel: RepoViewModel
    private lateinit var repoListAdapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PGSRepoApp.app.appComponent.inject(this)
        repoViewModel =
                ViewModelProviders.of(requireActivity(), repoVMFactory)
                    .get(RepoViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repoListAdapter = RepoListAdapter(listOf(), this)

        recyclerView.layoutManager = LinearLayoutManager(PGSRepoApp.app)
        swipeToRefresh.setOnRefreshListener { onRefresh() }
        deleteButton.setOnClickListener { repoViewModel.clearRepoList() }

        recyclerView.adapter = repoListAdapter
        repoViewModel.getRepoCount().observe(this, Observer {

            textRepoCount.text =
                    when {
                        ((it ?: 0) > 0) -> it.toString()
                        else -> getString(R.string.pull_to_refresh)
                    }
        })
        refreshAdapter()
    }


    fun onItemSelect(id: Int) {
        repoViewModel.setSelected(id)
        (activity as RepoListActivity).showDetail()
    }

    private fun onRefresh() {
        repoViewModel.onRefresh()
        repoViewModel.refreshState.observe(this, EventObserver {

            when (it) {
                is RepoDownloadStatus.DataOk -> {
                }
                is RepoDownloadStatus.ErrorMessage -> {
                    showError(it.message)
                }
                is RepoDownloadStatus.ErrorNoConnection -> {
                    showError(getString(R.string.no_internet_connection))
                }
                is RepoDownloadStatus.Forbidden -> {
                    showError(getString(R.string.rate_limit_exceeded))
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


