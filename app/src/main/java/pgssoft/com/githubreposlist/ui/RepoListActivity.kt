package pgssoft.com.githubreposlist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import pgssoft.com.githubreposlist.R

class RepoListActivity : AppCompatActivity(), RepoFragmentInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repolist)
        val fragmentList = RepoListFragment()
        supportFragmentManager.beginTransaction().apply {

            add(R.id.list, fragmentList)
            commit()
        }


        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val fragmentDetail = RepoDetailFragment()
            supportFragmentManager.beginTransaction().apply {
                add(R.id.detail, fragmentDetail)
                commit()
            }
        }

    }

    override fun onItemSelect(id: Int) {
        val fragmentDetail = RepoDetailFragment()
        val args = Bundle()
        args.putInt("id", id)
        fragmentDetail.arguments = args

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.detail, fragmentDetail)
                commit()

            }
        } else {

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.list, fragmentDetail)
                addToBackStack(null)
                commit()

            }
        }

    }

    override fun showError(message: String) {
        
        AlertDialog.Builder(this).setTitle(R.string.error).setMessage(message)
            .setPositiveButton("OK")
            { d, _ ->
                d.dismiss()
            }
            .create().show()
    }

}