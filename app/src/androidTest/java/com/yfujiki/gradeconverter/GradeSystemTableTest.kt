package com.yfujiki.gradeconverter

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class GradeSystemTableTest {

    var appContext: Context? = null

    fun setup() {
        appContext = InstrumentationRegistry.getTargetContext()
        GradeSystemTable.init(appContext!!)
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

    @Test
    fun testGradeAtIndex() {
        setup()

        val yosemiteGrade = GradeSystemTable.gradeSystemForNameCategory("Yosemite Decimal System", "Sports")
        assertEquals("Lowest grade should be 5.1", "5.1", yosemiteGrade?.gradeAtIndex(0, true))
        assertEquals("Highest grade should be 5.15c", "5.15c", yosemiteGrade?.gradeAtIndex(yosemiteGrade?.grades.count() - 1, false))

        val britishTechnical = GradeSystemTable.gradeSystemForNameCategory("British Technical", "Sports")
        assertEquals("Lowest grade should be 2", "2", britishTechnical?.gradeAtIndex(0, true))
        assertEquals("Highest grade should be 7b", "7b", britishTechnical?.gradeAtIndex(britishTechnical?.grades.count() - 1, false))
    }

    @Test
    fun testGradeAtIndexes() {
        setup()

        val yosemiteGrade = GradeSystemTable.gradeSystemForNameCategory("Yosemite Decimal System", "Sports")

        assertEquals("Grade should match", "5.1/5.2", yosemiteGrade?.localizedGradeAtIndexes((0..2).toList(), appContext!!))
        assertEquals("Grade should match", "5.10d ~ 5.11b", yosemiteGrade?.localizedGradeAtIndexes((20..23).toList(), appContext!!))
        assertEquals("Grade should match", "5.10d ~ 5.11b", yosemiteGrade?.localizedGradeAtIndexes((20..22).toList(), appContext!!))
        assertEquals("Grade should match", "5.11a/5.11b", yosemiteGrade?.localizedGradeAtIndexes((22..23).toList(), appContext!!))
    }

}