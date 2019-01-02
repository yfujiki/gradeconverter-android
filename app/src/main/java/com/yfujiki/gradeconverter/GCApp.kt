package com.yfujiki.gradeconverter

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences
import timber.log.Timber

class GCApp : Application() {
    companion object {
        private lateinit var instance: GCApp

        public fun getInstance(): GCApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initLeakCanary()
        configureData()

        instance = this
    }

    private fun configureData() {
        GradeSystemTable.init(applicationContext)
        LocalPreferences.init(applicationContext)

        if (LocalPreferences.selectedGradeSystems().size == 0) {
            LocalPreferences.setSelectedGradeSystems(
                    // ToDo : Use different locale and different country code
                    GradeSystemTable.gradeSystemsForCountryCode("US"),
                    false
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

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }
}