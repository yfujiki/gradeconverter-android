package com.yfujiki.gradeconverter.Utilities

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.yfujiki.gradeconverter.R
import java.util.*

class Localization {

    enum class Country(val value: Int) {
        US(0),
        JP(1),
        OTHERS(-1);

        fun string(): String {
            return when(this) {
                US -> "US"
                JP -> "JP"
                OTHERS -> "OTHERS"
            }
        }
    }

    enum class Lang(val value: Int) {
        EN(0),
        JA(1),
        OTHERS(-1);

        fun string(): String {
            return when(this) {
                EN -> "EN"
                JA -> "JA"
                OTHERS -> "OTHERS"
            }

        }
    }

    companion object {
        fun currentCountry(): Country {
            val countryCode = Locale.getDefault().isO3Country
            return when (countryCode) {
                "USA" -> Country.US
                "JPN" -> Country.JP
                else -> Country.OTHERS
            }
        }

        fun currentLang(): Lang {
            val langCode = Locale.getDefault().isO3Language
            return when (langCode) {
                "eng" -> Lang.EN
                "jpn" -> Lang.JA
                else -> Lang.OTHERS
            }
        }

        fun typefaceForCurrentLang(context: Context): Typeface? {
            val resourceId = when (currentLang()) {
                Lang.EN -> R.font.rounded_mplus_2p_light
                Lang.JA -> R.font.nicomoji_plus
                Lang.OTHERS -> R.font.rounded_mplus_2p_light
            }
            return ResourcesCompat.getFont(context, resourceId)
        }

        fun boldTypefaceForCurrentLang(context: Context): Typeface? {
            val resourceId = when (currentLang()) {
                Lang.EN -> R.font.rounded_mplus_2p_heavy
                Lang.JA -> R.font.nicomoji_plus
                Lang.OTHERS -> R.font.rounded_mplus_2p_heavy
            }
            return ResourcesCompat.getFont(context, resourceId)
        }
    }
}