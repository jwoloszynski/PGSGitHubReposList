package pgssoft.com.githubreposlist.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_item_view.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.getFormattedDate


class RepoListAdapter(var repoList: List<Repository>) : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {


    class RepoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(listRow: Repository) {

            repoName?.text = listRow.name
            repoDescription?.text = listRow.description

            repoUpdatedAt?.text = "Last updated: ${listRow.updated_at.getFormattedDate()}"

            repoPushedAt?.text = "Last pushed: ${listRow.pushed_at.getFormattedDate()}"
            repoLanguage?.text = listRow.language

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

        viewHolder.bind(repoList[index])

    }


    fun setData(list: List<Repository>) {
        repoList = list.sortedByDescending { it.pushed_at }
        notifyDataSetChanged()
    }


}