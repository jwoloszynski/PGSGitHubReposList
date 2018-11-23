package pgssoft.com.githubreposlist.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository

class RepoListAdapter(val repoList: List<Repository>) : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {
    private val sorted: MutableList<Repository> = mutableListOf()
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
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RepoViewHolder {
        val v: View = LayoutInflater.from(PGSRepoApp.app).inflate(R.layout.repo_item_view, p0, false)

        return RepoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return repoList.size

    }

    override fun onBindViewHolder(p0: RepoViewHolder, p1: Int) {
        p0.repoName?.text = (p1+1).toString()+". "+repoList.get(p1).name
        p0.repoDescription?.text = repoList.get(p1).description
        p0.repoUpdatedAt?.text = "Last updated: "  + repoList.get(p1).updated_at
        p0.repoPushedAt?.text = "Last pushed: " + repoList.get(p1).pushed_at

    }
}