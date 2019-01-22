package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_repolist.*
import kotlinx.android.synthetic.main.fragment_repo_detail.*
import pgssoft.com.githubreposlist.PGSRepoApp
import pgssoft.com.githubreposlist.R
import pgssoft.com.githubreposlist.data.db.Repository
import pgssoft.com.githubreposlist.viewmodels.RepoViewModel
import pgssoft.com.githubreposlist.viewmodels.RepoViewModelFactory
import javax.inject.Inject

/**
 * A fragment showing details of chosen repository
 */
class RepoDetailFragment : Fragment() {


    @Inject
    lateinit var repoVMFactory: RepoViewModelFactory
    private lateinit var repoViewModel: RepoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        PGSRepoApp.app.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        repoViewModel =
                ViewModelProviders.of(requireActivity(), repoVMFactory)
                    .get(RepoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteButton.visibility = View.INVISIBLE
        commentString.visibility = View.INVISIBLE

        repoViewModel.selected.observe(this, Observer {
            if (it != null) {
                updateView(it)

                with(requireActivity() as ReposActivity) {
                    menu?.findItem(R.id.action_like)?.icon =
                            if (it.liked == true) getDrawable(android.R.drawable.ic_input_delete)
                            else getDrawable(android.R.drawable.ic_input_add)
                    menu?.findItem(R.id.action_like)?.isVisible = true
                }
            }
        })

    }

    private fun updateView(repo: Repository) {
        repoName.text = repo.name
        repoDescription.text = repo.description
        repoComment.text = repo.comment
        if (repo.comment.isNullOrBlank()) {
            commentString.visibility = View.GONE
            noteButton.text = this.getText(R.string.add_note)
        } else {
            commentString.visibility = View.VISIBLE
            repoComment.visibility = View.VISIBLE
            noteButton.text = this.getText(R.string.edit_note)
        }
        noteButton.visibility = View.VISIBLE
        noteButton.setOnClickListener {
            (activity as ReposActivity).showNoteDialog(repo.id, repo.comment ?: "")
        }
    }
}

