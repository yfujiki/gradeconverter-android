package com.responsivebytes.gradeconverter

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.responsivebytes.gradeconverter.Models.GradeSystemTable
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import com.responsivebytes.gradeconverter.Models.LocalPreferencesImpl
import timber.log.Timber

class GCApp : Application() {
    companion object {
        private lateinit var instance: GCApp

        public fun getInstance(): GCApp {
            return instance
        }
    }

    var isTesting: Boolean = false

    lateinit var localPreferences: LocalPreferences

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initLeakCanary()
        configureData()

        instance = this
    }

    private fun configureData() {
        GradeSystemTable.init(applicationContext)
        localPreferences = LocalPreferencesImpl(applicationContext)
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