package com.yfujiki.gradeconverter

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class GradeSystemTableTest {

    @Test
    fun testInit() {
        val appContext = InstrumentationRegistry.getTargetContext()
        GradeSystemTable.init(appContext)

        assertEquals(16, GradeSystemTable.tableBody.count())
        assertTrue(GradeSystemTable.tableBody.keys.contains("Yosemite Decimal System-Sports"))
        assertTrue(GradeSystemTable.tableBody.keys.contains("Brazil-Boulder"))
        assertEquals(42, GradeSystemTable.tableBody["Yosemite Decimal System-Sports"]?.grades?.count())
    }
}