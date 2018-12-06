package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repo_list.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel


class RepoListFragment : Fragment() {

    private lateinit var listModel: RepoListViewModel

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
        deleteButton.setOnClickListener { listModel.clearRepoList() }

        recyclerView.adapter = repoListAdapter
        listModel = ViewModelProviders.of(activity!!).get(RepoListViewModel::class.java)
        listModel.getRepoCountText().observe(this, Observer { textRepoCount.text = it }
        )

        refreshAdapter()
    }

    private fun onRefresh() {
        listModel.onRefresh()

    }


    private fun refreshAdapter() {
        listModel.getRepoList().observe(this, Observer {

            repoListAdapter.setData(it!!)

        })

    }

    override fun onDestroyView() {

        swipeToRefresh.clearAnimation()
        super.onDestroyView()
    }
}


