package pgssoft.com.githubreposlist.utils

import java.text.SimpleDateFormat


fun String?.getFormattedDate(): String {
    if (this == null) return ""

    return try {
        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        var tmpDate = simpleDateFormat.parse(this)
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy, hh:mm:ss")
        simpleDateFormat.format(tmpDate)
    } catch (e: Exception) {
        this!!
    }


}
