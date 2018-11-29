package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repo_detail.*
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel


class RepoDetailFragment : Fragment() {

    lateinit var repoViewModel: RepoViewModel

    private var repoId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // Get back arguments
            repoId = if (arguments != null) {
                arguments!!.getInt("id", 1)
            } else {
                1
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repoViewModel = ViewModelProviders.of(this).get(RepoViewModel::class.java)
        repoViewModel.getRepoById(repoId)
        repoViewModel.getRepo().observe(this, Observer {
            updateView(it)
        })


    }

    private fun updateView(repo: Repository?) {
        repoName.text = repo?.name
        repoDescription.text = repo?.description
    }


}


