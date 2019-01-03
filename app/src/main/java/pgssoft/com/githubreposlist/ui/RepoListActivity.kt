package pgssoft.com.githubreposlist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pgssoft.com.githubreposlist.R

class RepoListActivity : AppCompatActivity() {

    val detailFragment = RepoDetailFragment()
    val listFragment = RepoListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_repolist)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    replace(R.id.detail, detailFragment)
                }
                replace(R.id.list, listFragment)
                commit()
            }

        }


    }

    fun showDetail() {

        supportFragmentManager.beginTransaction().apply {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                replace(R.id.detail, detailFragment)
            } else {
                replace(R.id.list, detailFragment)
            }
            addToBackStack(null)
            commit()

        }
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
        super.onBackPressed()
    }


}