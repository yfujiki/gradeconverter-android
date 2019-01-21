package com.responsivebytes.gradeconverter.Utilities

import android.content.Intent
import android.os.Build
import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri

object RatingUtility {
    fun rateApp(context: Context) {
        try {
            val rateIntent = rateIntentForUrl("market://details", context)
            context.startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details", context)
            context.startActivity(rateIntent)
        }
    }

    private fun rateIntentForUrl(url: String, context: Context): Intent {
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