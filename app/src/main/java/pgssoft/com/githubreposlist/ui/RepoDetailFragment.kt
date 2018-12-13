package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repo_detail.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.utils.PrefsHelper
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel


class RepoDetailFragment : Fragment(), KoinComponent {

    lateinit var repoViewModel: RepoViewModel
    private val prefs: PrefsHelper by inject()
    private var repoId = 0

    override fun onCreate(savedInstanceState: Bundle?) {

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
        noteButton.visibility = View.INVISIBLE
        commentString.visibility = View.INVISIBLE

        repoViewModel.repository.observe(this, Observer {

            if(it != null) {
                updateView(it)
            }
        })


    }

    private fun updateView(repo: Repository) {
        repoName.text = repo.name
        repoDescription.text = repo.description
        repoComment.text = repo.comment
        if (repo.comment.isNullOrBlank()) {
            commentString.visibility = View.INVISIBLE
            noteButton.text = this.getText(R.string.add_note)
        } else {
            commentString.visibility = View.VISIBLE
            noteButton.text = this.getText(R.string.edit_note)
        }

        noteButton.visibility = View.VISIBLE
        noteButton.setOnClickListener {
            (activity as RepoListActivity).showNoteDialog(repo.id, repo.comment?: "")
        }



    }


}


