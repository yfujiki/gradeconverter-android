package com.responsivebytes.gradeconverter.Utilities

import java.util.concurrent.atomic.AtomicBoolean

object TestDetector {
    private var _isRunningUITest: AtomicBoolean? = null

    @Synchronized
    fun isRunningUITest(): Boolean {
        if (null == _isRunningUITest) {
            var isTest: Boolean

            try {
                Class.forName("android.support.test.espresso.Espresso")
                isTest = true
            } catch (e: ClassNotFoundException) {
                isTest = false
            }

            _isRunningUITest = AtomicBoolean(isTest)
        }

        return _isRunningUITest!!.get()
    }
}