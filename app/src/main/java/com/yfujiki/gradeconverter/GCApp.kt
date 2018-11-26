package com.yfujiki.gradeconverter

import android.app.Application
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences

class GCApp: Application() {
    override fun onCreate() {
        super.onCreate()

        configureData()
    }

    private fun configureData() {
        GradeSystemTable.init(applicationContext)
        LocalPreferences.init(applicationContext)
        LocalPreferences.setSelectedGradeSystems(
                // ToDo : tailor for countries
                GradeSystemTable.gradeSystemsForCountryCode("US")
        )
        // ToDo : tailor for optimal index
        LocalPreferences.setCurrentIndexes(listOf(0))
    }
}