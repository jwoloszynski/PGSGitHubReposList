package pgssoft.com.githubreposlist.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repo_detail.*
import kotlinx.android.synthetic.main.tool_bar.*
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
    lateinit var repoViewModel: RepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        PGSRepoApp.app.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        repoViewModel =
                ViewModelProviders.of(requireActivity(), repoVMFactory)
                    .get(RepoViewModel::class.java)

        repoViewModel.selected.observe(this, Observer {
            if (it != null) {
                updateView(it)
                requireActivity().tool_bar.title = it.name.toString()
            }
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteButton.visibility = View.INVISIBLE
        commentString.visibility = View.INVISIBLE
    }

    private fun updateView(repo: Repository) {
        repoName.visibility = View.GONE
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
            (activity as ReposActivity).showNoteDialog(repo.id, repo.comment ?: "")
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        if (requireActivity().resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE && this.isVisible) {
            menu?.findItem(R.id.action_refresh)?.isVisible = false
            menu?.findItem(R.id.action_clearList)?.isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }
}
