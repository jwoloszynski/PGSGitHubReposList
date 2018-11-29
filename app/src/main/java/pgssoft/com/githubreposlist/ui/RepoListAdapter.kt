package pgssoft.com.githubreposlist.ui

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_item_view.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.RepoListDiffCallback
import pgssoft.com.githubreposlist.utils.getFormattedDate

class RepoListAdapter(private var repoList: List<Repository>) : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {


    constructor(repoList: List<Repository>, ctx: Context) : this(repoList) {
        repoListActivity = ctx as RepoListActivity

    }

    private var repoListActivity: RepoFragmentInterface? = null

    class RepoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(listRow: Repository, repoListActivity: RepoFragmentInterface?) {
            repoName?.text = listRow.name
            id?.text = listRow.id.toString()
            repoDescription?.text = listRow.description
            repoUpdatedAt?.text = "Last updated: ${listRow.updated_at.getFormattedDate()}"
            repoPushedAt?.text = "Last pushed: ${listRow.pushed_at.getFormattedDate()}"
            repoLanguage?.text = listRow.language

            detailsButton.setOnClickListener {
                repoListActivity?.onItemSelect(listRow.id)
            }


        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, element: Int): RepoViewHolder {
        val v: View = LayoutInflater.from(PGSRepoApp.app).inflate(R.layout.repo_item_view, viewGroup, false)

        return RepoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return repoList.size

    }

    override fun onBindViewHolder(viewHolder: RepoViewHolder, index: Int) {

        viewHolder.bind(repoList[index], repoListActivity)

    }


    fun setData(list: List<Repository>) {

        val diffResult = DiffUtil.calculateDiff(RepoListDiffCallback(list, repoList))
        repoList = list
        repoList = repoList.sortedByDescending { it.pushed_at }
        diffResult.dispatchUpdatesTo(this)

    }



}