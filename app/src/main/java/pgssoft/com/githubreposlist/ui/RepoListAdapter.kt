package pgssoft.com.githubreposlist.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository

class RepoListAdapter(var repoList:List<Repository>) : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {



    class RepoViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var repoName: TextView? = null
        var repoDescription: TextView? = null
        var repoUpdatedAt: TextView? = null
        var repoPushedAt: TextView? = null

        init {
            repoName = row.findViewById(R.id.repoName)
            repoDescription = row.findViewById(R.id.repoDescription)
            repoUpdatedAt = row.findViewById(R.id.repoUpdatedAt)
            repoPushedAt = row.findViewById(R.id.repoPushedAt)
        }

        fun bind(listRow: Repository, index: Int) {
            repoName?.text = (index + 1).toString() + ". " + listRow.name
            repoDescription?.text = listRow.description
            repoUpdatedAt?.text = "Last updated: " + listRow.updated_at
            repoPushedAt?.text = "Last pushed: " + listRow.pushed_at
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

        viewHolder.bind(repoList[index], index)


    }
}