package pgssoft.com.githubreposlist.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_repo_list.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.EventObserver
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.services.ReposFetchingService
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
    private lateinit var alarmManager: AlarmManager
    private lateinit var pIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PGSRepoApp.app.appComponent.inject(this)
        repoViewModel =
                ViewModelProviders.of(requireActivity(), repoVMFactory)
                    .get(RepoViewModel::class.java)
        setRepeatingFetching()

        ReposFetchingService.statusEvent.observe(this, getObserver(true))

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
        repoViewModel.refreshState.observe(this, getObserver())

        swipeToRefresh.isRefreshing = false

    }

    private fun refreshRepoList() {
        repoViewModel.getRepoList().observe(this, Observer {
            if (it != null) {
                repoListAdapter.setData(it)
            }
            val count = it?.count() ?: 0
            val likedCount = it?.filter { it.liked == true }?.count() ?: 0
            textRepoCount.text =
                    when {
                        ((count) > 0) -> "$count,  likes count: $likedCount"
                        else -> getString(R.string.pull_to_refresh)
                    }
        })
    }

    private fun showError(message: String, silent: Boolean = false) {
        if (!silent) {
            AlertDialog.Builder(requireActivity(), R.style.PGSAppAlertDialog).setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.ok)
                { d, _ ->
                    d.dismiss()
                }
                .create().show()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
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

    private fun setRepeatingFetching() {

        val i = Intent(requireContext(), ReposFetchingService::class.java)
        pIntent = PendingIntent.getService(requireContext(), 3434, i, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
            AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        alarmManager.cancel(pIntent)
    }

    private fun getObserver(silent: Boolean = false) = EventObserver<RepoDownloadStatus> {
        when (it) {
            is RepoDownloadStatus.DataOk -> {
                Toast.makeText(requireContext(), "Data ok", Toast.LENGTH_SHORT).show()
            }
            is RepoDownloadStatus.ErrorMessage -> {
                showError(it.message, silent)
            }
            is RepoDownloadStatus.ErrorNoConnection -> {
                showError(getString(R.string.no_internet_connection), silent)
            }
            is RepoDownloadStatus.Forbidden -> {
                showError(getString(R.string.rate_limit_exceeded), silent)
            }
        }
    }
}
