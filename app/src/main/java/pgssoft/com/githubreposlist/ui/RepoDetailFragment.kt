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
    lateinit var repoViewModel: RepoViewModel
    private var menu: Menu? = null
    private var toolbarTitle: String = ""
    private var localRepo: Repository? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        PGSRepoApp.app.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        repoViewModel =
                ViewModelProviders.of(requireActivity(), repoVMFactory)
                    .get(RepoViewModel::class.java)

        repoViewModel.selected.observe(this, Observer {
            if (it != null) {
                updateView(it)
                localRepo = it
                toolbarTitle = it.name.toString()
                menu?.findItem(R.id.action_like)?.title = if(it.liked == true) "Unlike" else "Like"
                requireActivity().tool_bar.title = toolbarTitle
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteButton.visibility = View.INVISIBLE
        commentString.visibility = View.INVISIBLE
        menu?.findItem(R.id.action_like)?.isVisible = false

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
            (activity as ReposActivity).showNoteDialog(repo.id, repo.comment ?: "")
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        this.menu = menu!!
        menu.findItem(R.id.action_like)?.isVisible = (repoViewModel.isSelected)

        if (this.isVisible && resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            menu.findItem(R.id.action_refresh)?.isVisible = false
            menu.findItem(R.id.action_clearList)?.isVisible = false
        }

    }
}
