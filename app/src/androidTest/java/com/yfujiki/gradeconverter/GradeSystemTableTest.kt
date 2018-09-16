package com.yfujiki.gradeconverter

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class GradeSystemTableTest {

    fun setup() {
        val appContext = InstrumentationRegistry.getTargetContext()
        GradeSystemTable.init(appContext)
    }

    @Test
    fun testInit() {
        setup()

        assertEquals(16, GradeSystemTable.tableBody.count())
        assertTrue(GradeSystemTable.tableBody.keys.contains("Yosemite Decimal System-Sports"))
        assertTrue(GradeSystemTable.tableBody.keys.contains("Brazil-Boulder"))
        assertEquals(42, GradeSystemTable.tableBody["Yosemite Decimal System-Sports"]?.grades?.count())
    }

    @Test
    fun testGradeSystemForNameCategory() {
        setup()

        assertEquals("Brazil",
                GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")?.name)
        assertEquals("Sports",
                GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")?.category)
        assertEquals("Brazil",
                GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")?.name)
        assertEquals("Boulder",
                GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")?.category)

    }
}