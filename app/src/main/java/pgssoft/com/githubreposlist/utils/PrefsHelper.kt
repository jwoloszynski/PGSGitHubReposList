package pgssoft.com.githubreposlist.utils

import android.content.Context
import android.preference.PreferenceManager

class PrefsHelper(ctx: Context) {


    private val preferences = PreferenceManager.getDefaultSharedPreferences(ctx)

    var time = preferences.getLong("time", -1L)
        set(time) {
            preferences.edit().putLong("time", time).apply()
        }

    var repoId: Int = preferences.getInt("repoId", 0)
        set(repoId) {
            preferences.edit().putInt("repoId", repoId).apply()
        }


    fun clearAll(){
        time = 0
        repoId = 0
    }
}