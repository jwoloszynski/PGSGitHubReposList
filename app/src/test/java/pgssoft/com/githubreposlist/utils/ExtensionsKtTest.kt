package pgssoft.com.githubreposlist.utils

import org.junit.Assert
import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun getFormattedDateDateString() {
        Assert.assertTrue("2018-12-12T08:56:17Z".getFormattedDate() == "12-12-2018, 08:56:17")
    }

    @Test
    fun getFormattedDateNotDateString() {
        val string = "random test string with no date"
        Assert.assertTrue(string.getFormattedDate() == string)
    }

    @Test
    fun getFormattedDateEmptyString() {
        Assert.assertTrue("".getFormattedDate() == "")
    }

    @Test
    fun getFormattedDateNullString() {
        val string: String? = null
        Assert.assertTrue(string.getFormattedDate() == "")
    }
}
