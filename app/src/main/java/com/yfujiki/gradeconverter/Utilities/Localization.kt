package com.yfujiki.gradeconverter.Utilities

import android.content.Context
import android.graphics.Typeface
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

        fun fontFileForCurrentLang(): String {
            return when (currentLang()) {
                Lang.EN -> "font/rounded_mplus_2p_light.ttf"
                Lang.JA -> "font/nicomoji_plus.ttf"
                Lang.OTHERS -> "font/rounded_mplus_2p_light.ttf"
            }
        }

        fun boldFontFileForCurrentLang(): String {
            return when (currentLang()) {
                Lang.EN -> "font/rounded_mplus_2p_heavy.ttf"
                Lang.JA -> "font/nicomoji_plus.ttf"
                Lang.OTHERS -> "font/rounded_mplus_2p_heavy.ttf"
            }
        }

        fun typefaceForCurrentLang(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, fontFileForCurrentLang())
        }

        fun boldTypefaceForCurrentLang(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, boldFontFileForCurrentLang())
        }
    }
}