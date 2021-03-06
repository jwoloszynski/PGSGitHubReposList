package pgssoft.com.githubreposlist.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * An extension function to format date from string provided by api
 */
fun String?.getFormattedDate(): String {
    if (this == null) return ""

    return try {
        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale("pl_PL"))
        val tmpDate = simpleDateFormat.parse(this)
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy, hh:mm:ss", Locale("pl"))
        simpleDateFormat.format(tmpDate)
    } catch (e: Exception) {
        this
    }
}
