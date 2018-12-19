package pgssoft.com.githubreposlist.utils

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

class PrefsHelper(ctx: Context) {


    private val preferences = PreferenceManager.getDefaultSharedPreferences(ctx)
    private val editor = preferences.edit()
    var time = preferences.getLong("time", -1L)
        set(time) {
            editor.remove("time")
            editor.commit()
            editor.putLong("time", time)
            editor.commit()
        }

    var repoId: Int = preferences.getInt("repoId", 0)
        set(repoId) {
            editor.putInt("repoId", repoId)
            editor.commit()
        }


    fun clearAll(){
        time = 0
        repoId = 0
    }
}