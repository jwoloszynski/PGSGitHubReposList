package pgssoft.com.githubreposlist.utils

import android.support.v7.util.DiffUtil
import pgssoft.com.githubreposlist.data.db.Repository

/**
 * Callback used for DiffUtils in list adapter
 */
class RepoListDiffCallback(var newList: List<Repository>, var oldList: List<Repository>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == (newList[newItemPosition].id)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
