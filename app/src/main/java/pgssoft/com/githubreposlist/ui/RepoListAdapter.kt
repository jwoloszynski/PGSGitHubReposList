package pgssoft.com.githubreposlist.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_item_view.*
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.RepoListDiffCallback
import pgssoft.com.githubreposlist.utils.getFormattedDate

class RepoListAdapter(private var repoList: List<Repository>, private var fragment: RepoListFragment) :
    RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {

    class RepoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(listRow: Repository, fragment: RepoListFragment) {
            repoName?.text = listRow.name
            id?.text = listRow.id.toString()
            repoDescription?.text = listRow.description
            repoUpdatedAt?.text =
                    fragment.resources.getString(R.string.last_updated, listRow.updated_at.getFormattedDate())
            repoPushedAt?.text =
                    fragment.resources.getString(R.string.last_pushed, listRow.pushed_at.getFormattedDate())
            repoLanguage?.text = listRow.language

            noteButton.text = if (listRow.comment.isNullOrEmpty()) {
                fragment.resources.getString(R.string.add_note)
            } else {
                fragment.resources.getString(R.string.edit_note)
            }
            detailsButton.setOnClickListener {
                fragment.onItemSelect(listRow.id)
            }
            noteButton.setOnClickListener {
                (fragment.activity as ReposActivity).showNoteDialog(listRow.id, listRow.comment ?: " ")
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, element: Int): RepoViewHolder {
        val v: View = LayoutInflater.from(fragment.context).inflate(R.layout.repo_item_view, viewGroup, false)
        return RepoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(viewHolder: RepoViewHolder, index: Int) {
        viewHolder.bind(repoList[index], fragment)
    }

    fun setData(list: List<Repository>) {
        val diffResult = DiffUtil.calculateDiff(RepoListDiffCallback(list, repoList))
        repoList = list
        repoList = repoList.sortedWith(compareByDescending { it.pushed_at })
        diffResult.dispatchUpdatesTo(this)
    }


}
