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
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel


class RepoListFragment : Fragment() {

    lateinit var listModel: RepoListViewModel

    private val repoListAdapter: RepoListAdapter = RepoListAdapter(listOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

    private fun listenErrors() {

        listModel.repoListErrorText.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showErrorMessage(it)
            }

            swipeToRefresh.isRefreshing = false
        })

    }

    private fun refreshAdapter() {
        listModel.getRepoList().observe(this, Observer {

            repoListAdapter.setData(it!!)
            recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, 0)

        })

    }

    private fun showErrorMessage(errorMessage: String) {

        AlertDialog.Builder(activity!!.applicationContext).setTitle(R.string.error).setMessage(errorMessage)
            .setPositiveButton("OK")
            { d, _ ->
                d.dismiss()
            }
            .create().show()

    }

 interface OnButtonDetailClicked {

     fun onButtonClicked(position:Int)
 }
}


