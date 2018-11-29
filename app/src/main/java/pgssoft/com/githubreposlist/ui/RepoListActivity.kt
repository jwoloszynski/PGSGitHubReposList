package pgssoft.com.githubreposlist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pgssoft.com.githubreposlist.R

class RepoListActivity:AppCompatActivity(), RepoListFragment.OnButtonDetailClicked {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repolist)
        val fragmentManager = supportFragmentManager
        val fragmentList = RepoListFragment()
        fragmentManager.beginTransaction().apply {

            add(R.id.list, fragmentList)
            commit()
        }


        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val fragmentDetail = RepoDetailFragment()
            fragmentManager.beginTransaction().apply {
                add(R.id.detail, fragmentDetail)
                commit()
            }
        }

    }

    override fun onButtonClicked(position: Int) {

    }
}