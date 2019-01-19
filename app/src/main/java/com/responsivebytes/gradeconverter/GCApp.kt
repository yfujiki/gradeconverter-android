package com.responsivebytes.gradeconverter

import android.app.Activity
import android.app.Application
import com.responsivebytes.gradeconverter.Dagger.DaggerAppComponent
import com.responsivebytes.gradeconverter.Dagger.DaggerAppComponentUITest
import com.squareup.leakcanary.LeakCanary
import com.responsivebytes.gradeconverter.Models.GradeSystemTable
import com.responsivebytes.gradeconverter.Utilities.TestDetector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import javax.inject.Inject

class GCApp : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

    companion object {
        private lateinit var instance: GCApp

        public fun getInstance(): GCApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        initDagger()
        initTimber()
        initLeakCanary()
        configureData()

        instance = this
    }

    private fun initDagger() {
        if (TestDetector.isRunningUITest()) {
            DaggerAppComponentUITest.builder()
                    .create(this)
                    .inject(this)
        } else {
            DaggerAppComponent.builder()
                .create(this)
                .inject(this)
        }
    }

    private fun configureData() {
        GradeSystemTable.init(applicationContext)
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