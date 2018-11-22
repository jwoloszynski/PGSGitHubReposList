package pgssoft.com.githubreposlist.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import pgssoft.com.githubreposlist.data.db.Repository

class RepoListAdapter (val repoList: List<Repository>): RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>()  {

    class RepoViewHolder(val tv: TextView): RecyclerView.ViewHolder(tv){

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RepoViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return repoList.size

    }

    override fun onBindViewHolder(p0: RepoViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}