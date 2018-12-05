package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repo_detail.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel


class RepoDetailFragment : Fragment() {

    lateinit var repoViewModel: RepoViewModel
    lateinit var prefs: PrefsHelper
    private var repoId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = PrefsHelper(PGSRepoApp.app)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // Get back arguments
            repoId = if (arguments != null) {
                arguments!!.getInt("id", prefs.repoId)

            } else {
                prefs.repoId
            }
            prefs.repoId = repoId
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repoViewModel = ViewModelProviders.of(activity!!).get(RepoViewModel::class.java)
        if (repoId == 0) {
            repoId = prefs.repoId
        }
        repoViewModel.getRepoById(repoId).observe(this, Observer {

            updateView(it)
        })


    }

    private fun updateView(repo: Repository?) {
        repoName.text = repo?.name
        repoDescription.text = repo?.description
        repoComment.text = repo?.comment
        commentString.visibility = if (repo?.comment.isNullOrBlank()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }


}

