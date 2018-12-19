package pgssoft.com.githubreposlist.utils

import org.junit.Assert
import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun stringFormatToDateTest() {

        Assert.assertTrue("2018-12-12T08:56:17Z".getFormattedDate() == "12-12-2018, 08:56:17")
        Assert.assertFalse("2018-12-12T08:56:17Z".getFormattedDate() == "12-12-2013, 08:56:17")
        Assert.assertTrue("asdasd".getFormattedDate() == "")
    }
}