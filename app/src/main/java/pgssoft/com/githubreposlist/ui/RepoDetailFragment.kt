package pgssoft.com.githubreposlist.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.viewmodels.RepoListViewModel


class RepoDetailFragment : Fragment() {

    lateinit var listModel: RepoListViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



    }









}


