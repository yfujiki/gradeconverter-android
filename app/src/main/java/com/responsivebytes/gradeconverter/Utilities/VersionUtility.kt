package com.responsivebytes.gradeconverter.Utilities

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.lang.StringBuilder

object VersionUtility {
    fun getVersionName(context: Context): String {
        try {
            val pInfo = context.packageManager
                    .getPackageInfo(context.packageName, 0)
            val builder = StringBuilder()
            builder.append("Version : ")
            builder.append(pInfo.versionName)
            builder.append("(")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                builder.append(pInfo.longVersionCode)
            } else {
                builder.append(pInfo.versionCode)
            }
            builder.append(")")
            return builder.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            return "Error"
        }
    }
}