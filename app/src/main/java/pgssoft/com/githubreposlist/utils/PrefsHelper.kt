package pgssoft.com.githubreposlist.utils

import android.content.Context
import android.preference.PreferenceManager

class PrefsHelper(ctx: Context) {


    private val preferences = PreferenceManager.getDefaultSharedPreferences(ctx)

    var time = preferences.getLong("time", -1L)
        set(time) {
            preferences.edit().putLong("time", time).apply()
        }


}