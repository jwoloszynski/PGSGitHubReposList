package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_repolist.*
import kotlinx.android.synthetic.main.fragment_repo_list.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.EventObserver
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel
import pgssoft.com.githubreposlist.viewmodels.RepoViewModelFactory
import javax.inject.Inject


/**
 * Displays a list of company GitHub repositories
 */
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

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repoListAdapter = RepoListAdapter(listOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(PGSRepoApp.app)
        swipeToRefresh.setOnRefreshListener { onRefresh() }
        recyclerView.adapter = repoListAdapter
        refreshRepoList()
    }

    fun onItemSelect(id: Int) {

        repoViewModel.setSelected(id)
        (activity as ReposActivity).showDetail()
    }

    fun onRefresh() {
        swipeToRefresh.isRefreshing = true
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

    private fun refreshRepoList() {
        repoViewModel.getRepoList().observe(this, Observer {
            if (it != null) {
                repoListAdapter.setData(it)
            }
            val count = it?.count() ?: 0
            val likedcount = it?.filter { it.liked == true }?.count() ?: 0
            textRepoCount.text =
                    when {
                        ((count) > 0) -> "$count,  likes count: $likedcount"
                        else -> getString(R.string.pull_to_refresh)
                    }
        })
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireActivity(), R.style.PGSAppAlertDialog).setTitle(R.string.error).setMessage(message)
            .setPositiveButton(R.string.ok)
            { d, _ ->
                d.dismiss()
            }
            .create().show()
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_repolist, menu)
        menu?.findItem(R.id.action_like)?.isVisible = false

    }

    fun clearRepoList() {

        AlertDialog.Builder(requireContext(), R.style.PGSAppAlertDialog).setTitle("Are you sure?")
            .setMessage("You will lose all your comments")
            .setPositiveButton("Yes") { _, _ ->
                repoViewModel.clearRepoList()
            }
            .setNeutralButton("No") { dialog: DialogInterface, _ ->
                dialog.cancel()
            }
            .create().show()
    }


    override fun onPrepareOptionsMenu(menu: Menu?) {
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            menu?.findItem(R.id.action_like)?.isVisible = false
            requireActivity().tool_bar.title = "GitHubRepoList"
        }

        super.onPrepareOptionsMenu(menu)
    }
}
