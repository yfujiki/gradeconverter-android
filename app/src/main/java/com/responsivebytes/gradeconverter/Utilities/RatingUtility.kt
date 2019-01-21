package com.responsivebytes.gradeconverter.Utilities

import android.content.Intent
import android.os.Build
import android.content.ActivityNotFoundException
import android.net.Uri
import com.responsivebytes.gradeconverter.GCApp

object RatingUtility {
    fun rateApp() {
        val context = GCApp.getInstance().applicationContext
        try {
            val rateIntent = rateIntentForUrl("market://details")
            context.startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details")
            context.startActivity(rateIntent)
        }
    }

    private fun rateIntentForUrl(url: String): Intent {
        val context = GCApp.getInstance().applicationContext
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())))
        var flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        if (Build.VERSION.SDK_INT >= 21) {
            flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags = flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
        }
        intent.addFlags(flags)
        return intent
    }
}