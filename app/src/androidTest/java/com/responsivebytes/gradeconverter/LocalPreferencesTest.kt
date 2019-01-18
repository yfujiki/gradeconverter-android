package com.responsivebytes.gradeconverter

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.responsivebytes.gradeconverter.Models.GradeSystemTable
import com.responsivebytes.gradeconverter.Models.LocalPreferencesImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class LocalPreferencesTest {

    var appContext: Context? = null

    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        GradeSystemTable.init(appContext!!)
        GCApp.getInstance().localPreferences = LocalPreferencesImpl(appContext!!)
        GCApp.getInstance().localPreferences.setCurrentIndexes(listOf())
        GCApp.getInstance().localPreferences.setSelectedGradeSystems(listOf(), false)
    }

    fun tearDown() {
    }

    @Test
    fun testCurrentIndexes() {
        setup()

        val indexes = listOf(1, 2, 3)
        GCApp.getInstance().localPreferences.setCurrentIndexes(indexes)
        val currentIndexes = GCApp.getInstance().localPreferences.currentIndexes()

        assertEquals(indexes, currentIndexes)
    }

    @Test
    fun testAddGradeSystem() {
        setup()

        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        GCApp.getInstance().localPreferences.addSelectedGradeSystem(gradeSystem1)

        assertEquals(1, GCApp.getInstance().localPreferences.selectedGradeSystems().size)
        assertEquals(gradeSystem1, GCApp.getInstance().localPreferences.selectedGradeSystems()[0])
    }

    @Test
    fun testRemoveGradeSystem() {
        setup()

        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        GCApp.getInstance().localPreferences.addSelectedGradeSystem(gradeSystem1)

        val gradeSystem2 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")!!
        GCApp.getInstance().localPreferences.addSelectedGradeSystem(gradeSystem2)

        assertEquals(2, GCApp.getInstance().localPreferences.selectedGradeSystems().size)

        GCApp.getInstance().localPreferences.removeSelectedGradeSystem(gradeSystem1)

        assertEquals(1, GCApp.getInstance().localPreferences.selectedGradeSystems().size)
        assertEquals(gradeSystem2, GCApp.getInstance().localPreferences.selectedGradeSystems()[0])

        GCApp.getInstance().localPreferences.removeSelectedGradeSystem(gradeSystem2)
        assertEquals(0, GCApp.getInstance().localPreferences.selectedGradeSystems().size)
    }

    @Test
    fun testSelectedGradeSystemNamesCsv() {
        setup()

        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        GCApp.getInstance().localPreferences.addSelectedGradeSystem(gradeSystem1)

        val gradeSystem2 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")!!
        GCApp.getInstance().localPreferences.addSelectedGradeSystem(gradeSystem2)

        val gradeSystemNamesCSV = GCApp.getInstance().localPreferences.selectedGradeSystemNamesCSV()
        assertEquals("Brazil, Brazil", gradeSystemNamesCSV)
    }
}