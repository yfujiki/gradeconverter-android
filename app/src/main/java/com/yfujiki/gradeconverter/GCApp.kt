package com.yfujiki.gradeconverter

import android.app.Application
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences
import timber.log.Timber

class GCApp: Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()

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

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}