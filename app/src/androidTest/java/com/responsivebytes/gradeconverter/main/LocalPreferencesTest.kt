package com.responsivebytes.gradeconverter.main

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.responsivebytes.gradeconverter.Models.GradeSystemTable
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import com.responsivebytes.gradeconverter.Models.LocalPreferencesImpl
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class LocalPreferencesTest {

    var appContext: Context? = null
    var localPreferences: LocalPreferences? = null

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        GradeSystemTable.init(appContext!!)

        localPreferences = LocalPreferencesImpl(appContext!!, "Unit Test")
        localPreferences!!.setCurrentIndexes(listOf())
        localPreferences!!.setSelectedGradeSystems(listOf(), false)
    }

    @After
    fun tearDown() {
        localPreferences!!.reset()
    }

    @Test
    fun testCurrentIndexes() {
        val indexes = listOf(1, 2, 3)
        localPreferences!!.setCurrentIndexes(indexes)
        val currentIndexes = localPreferences!!.currentIndexes()

        assertEquals(indexes, currentIndexes)
    }

    @Test
    fun testAddGradeSystem() {
        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        localPreferences!!.addSelectedGradeSystem(gradeSystem1)

        assertEquals(1, localPreferences!!.selectedGradeSystems().size)
        assertEquals(gradeSystem1, localPreferences!!.selectedGradeSystems()[0])
    }

    @Test
    fun testRemoveGradeSystem() {
        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        localPreferences!!.addSelectedGradeSystem(gradeSystem1)

        val gradeSystem2 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")!!
        localPreferences!!.addSelectedGradeSystem(gradeSystem2)

        assertEquals(2, localPreferences!!.selectedGradeSystems().size)

        localPreferences!!.removeSelectedGradeSystem(gradeSystem1)

        assertEquals(1, localPreferences!!.selectedGradeSystems().size)
        assertEquals(gradeSystem2, localPreferences!!.selectedGradeSystems()[0])

        localPreferences!!.removeSelectedGradeSystem(gradeSystem2)
        assertEquals(0, localPreferences!!.selectedGradeSystems().size)
    }

    @Test
    fun testSelectedGradeSystemNamesCsv() {
        val gradeSystem1 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Sports")!!
        localPreferences!!.addSelectedGradeSystem(gradeSystem1)

        val gradeSystem2 = GradeSystemTable.gradeSystemForNameCategory("Brazil", "Boulder")!!
        localPreferences!!.addSelectedGradeSystem(gradeSystem2)

        val gradeSystemNamesCSV = localPreferences!!.selectedGradeSystemNamesCSV()
        assertEquals("Brazil, Brazil", gradeSystemNamesCSV)
    }
}