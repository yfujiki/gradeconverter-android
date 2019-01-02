package com.yfujiki.gradeconverter

import android.app.Application
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences
import timber.log.Timber

class GCApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()

        configureData()
    }

    private fun configureData() {
        GradeSystemTable.init(applicationContext)
        LocalPreferences.init(applicationContext)

        if (LocalPreferences.selectedGradeSystems().size == 0) {
            LocalPreferences.setSelectedGradeSystems(
                    // ToDo : Use different locale and different country code
                    GradeSystemTable.gradeSystemsForCountryCode("US")
            )
        }

        if (LocalPreferences.currentIndexes().size == 0) {
            // ToDo : Check the number for iOS
            LocalPreferences.setCurrentIndexes(listOf(10))
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}