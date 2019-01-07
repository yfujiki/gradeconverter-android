package com.responsivebytes.gradeconverter

import android.os.ParcelFileDescriptor
import android.support.test.InstrumentationRegistry
import java.io.BufferedReader
import java.io.InputStreamReader

class DemoModeEnabler {

    fun enable() {
        executeShellCommand("settings put global sysui_demo_allowed 1")
        sendCommand("exit")
        sendCommand("enter")
        sendCommand("notifications", "visible" to "false")
        sendCommand("network", "wifi" to "show", "level" to "4")
        sendCommand("network", "mobile" to "show", "datatype" to "4g", "level" to "4")
        sendCommand("battery", "level" to "100", "plugged" to "false")
        sendCommand("clock", "hhmm" to "1200")
    }

    fun disable() {
        sendCommand("exit")
    }

    private fun sendCommand(command: String, vararg extras: Pair<String, Any>) {
        val exec = StringBuilder("am broadcast -a com.android.systemui.demo -e command $command")
        for ((key, value) in extras) {
            exec.append(" -e $key $value")
        }
        executeShellCommand(exec.toString())
    }

    private fun executeShellCommand(command: String) {
        waitForCompletion(InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(command))
    }

    private fun waitForCompletion(descriptor: ParcelFileDescriptor) {
        val reader = BufferedReader(InputStreamReader(ParcelFileDescriptor.AutoCloseInputStream(descriptor)))
        reader.use {
            it.readText()
        }
    }
}