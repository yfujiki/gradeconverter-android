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
                GradeSystemTable.gradeSystemsForNameCategory("Brazil", "Sports")?.name)
        assertEquals("Sports",
                GradeSystemTable.gradeSystemsForNameCategory("Brazil", "Sports")?.category)
        assertEquals("Brazil",
                GradeSystemTable.gradeSystemsForNameCategory("Brazil", "Boulder")?.name)
        assertEquals("Boulder",
                GradeSystemTable.gradeSystemsForNameCategory("Brazil", "Boulder")?.category)

    }

    @Test
    fun testGradeSystemForCountyCode() {
        setup()

        val gradeSystemsForUS = GradeSystemTable.gradeSystemsForCountryCode("US")
        assertEquals("US grade should have Hueco and Yosemite",2, gradeSystemsForUS.count())
        assertEquals("US grade should have Hueo and Yosemite", "Hueco", gradeSystemsForUS[0].name)
        assertEquals("US grade should have Hueo and Yosemite", "Yosemite Decimal System", gradeSystemsForUS[1].name)

        val gradeSystemsForJP = GradeSystemTable.gradeSystemsForCountryCode("JP")
        assertEquals("Japan Grade should have Ogawayama and Toyota", 2, gradeSystemsForJP.count())
        assertEquals("Japan Grade should have Ogawayama and Toyota", "Ogawayama", gradeSystemsForJP[0].name)
        assertEquals("Japan Grade should have Ogawayama and Toyota", "Toyota", gradeSystemsForJP[1].name)
    }
}