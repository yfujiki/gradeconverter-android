package com.responsivebytes.gradeconverter

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.responsivebytes.gradeconverter.Models.GradeSystemTable
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class LocalPreferencesTest {

    var appContext: Context? = null

    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        GradeSystemTable.init(appContext!!)
        LocalPreferences.init(appContext!!)
        LocalPreferences.setCurrentIndexes(listOf())
        LocalPreferences.setSelectedGradeSystems(listOf(), false)
    }

    fun tearDown() {
    }

    @Test
    fun testCurrentIndexes() {
        setup()

        val indexes = listOf(1, 2, 3)
        LocalPreferences.setCurrentIndexes(indexes)
        val currentIndexes = LocalPreferences.currentIndexes()

        assertEquals(indexes, currentIndexes)
    }

    @Test
    fun testAddGradeSystem() {
        setup()

        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        LocalPreferences.addSelectedGradeSystem(gradeSystem1)

        assertEquals(1, LocalPreferences.selectedGradeSystems().size)
        assertEquals(gradeSystem1, LocalPreferences.selectedGradeSystems()[0])
    }

    @Test
    fun testRemoveGradeSystem() {
        setup()

        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        LocalPreferences.addSelectedGradeSystem(gradeSystem1)

        val gradeSystem2 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")!!
        LocalPreferences.addSelectedGradeSystem(gradeSystem2)

        assertEquals(2, LocalPreferences.selectedGradeSystems().size)

        LocalPreferences.removeSelectedGradeSystem(gradeSystem1)

        assertEquals(1, LocalPreferences.selectedGradeSystems().size)
        assertEquals(gradeSystem2, LocalPreferences.selectedGradeSystems()[0])

        LocalPreferences.removeSelectedGradeSystem(gradeSystem2)
        assertEquals(0, LocalPreferences.selectedGradeSystems().size)
    }

    @Test
    fun testSelectedGradeSystemNamesCsv() {
        setup()

        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        LocalPreferences.addSelectedGradeSystem(gradeSystem1)

        val gradeSystem2 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")!!
        LocalPreferences.addSelectedGradeSystem(gradeSystem2)

        val gradeSystemNamesCSV = LocalPreferences.selectedGradeSystemNamesCSV()
        assertEquals("Brazil, Brazil", gradeSystemNamesCSV)
    }
}