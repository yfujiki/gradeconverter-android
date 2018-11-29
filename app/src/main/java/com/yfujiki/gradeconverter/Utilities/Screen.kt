package com.yfujiki.gradeconverter.Utilities

import android.content.Context
import android.widget.Toast
import android.util.DisplayMetrics



class Screen {
    companion object {
        fun isSparseScreen(context: Context): Boolean {
            val density = context.getResources().getDisplayMetrics().densityDpi

            when (density) {
                DisplayMetrics.DENSITY_LOW -> return true
                DisplayMetrics.DENSITY_MEDIUM -> return true
                DisplayMetrics.DENSITY_HIGH -> return true
                else -> return false
            }
        }
    }
}