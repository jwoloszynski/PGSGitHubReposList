package pgssoft.com.githubreposlist.utils

import android.os.Bundle
import android.support.v7.util.DiffUtil
import pgssoft.com.githubreposlist.data.db.Repository

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

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

//        return super.getChangePayload(oldItemPosition, newItemPosition)

// not sure if needed
        var oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        var bundle = Bundle()



        if (oldItem.name != newItem.name) {
            bundle.putString("name", newItem.name)
        }

        if (oldItem.description != newItem.description) {
            bundle.putString("description", newItem.description)
        }

        if (oldItem.language != newItem.language) {
            bundle.putString("language", newItem.language)
        }

        if (oldItem.pushed_at != newItem.pushed_at) {
            bundle.putString("pushed_at", newItem.pushed_at)
        }

        if (oldItem.updated_at != newItem.updated_at) {
            bundle.putString("pushed_at", newItem.updated_at)
        }

        return if (bundle.size() > 0) {
             bundle
        }
        else {
            null
        }

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition] == newList[newItemPosition]

    }
}