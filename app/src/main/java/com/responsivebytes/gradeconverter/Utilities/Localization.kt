package com.responsivebytes.gradeconverter.Utilities

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.responsivebytes.gradeconverter.R
import java.util.*
import kotlin.collections.HashMap

class Localization {

    enum class Country(val value: Int) {
        US(0),
        JP(1),
        OTHERS(-1);

        fun string(): String {
            return when (this) {
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
            return when (this) {
                EN -> "EN"
                JA -> "JA"
                OTHERS -> "OTHERS"
            }
        }
    }

    companion object {
        private val stringToResourceMap: HashMap<String, Int> = hashMapOf(
                "Yosemite Decimal System" to R.string.yosemite_decimal_system,
                "British Technical" to R.string.british_technical,
                "British Adjective" to R.string.british_adjective,
                "French" to R.string.french,
                "UIAA" to R.string.uiaa,
                "SAXON" to R.string.saxon,
                "Ewbank AUS/NZ" to R.string.ewbank_aus_nz,
                "Ewbank South Africa" to R.string.ewbank_south_africa,
                "Nordic FIN" to R.string.nordic_fin,
                "Nordic SWE/NOR" to R.string.nordic_swe_nor,
                "Brazil" to R.string.brazil,
                "Hueco" to R.string.hueco,
                "Ogawayama" to R.string.ogawayama,
                "Fontainebleu" to R.string.fontainebleu,
                "toyota" to R.string.toyota,
                "10-kyu" to R.string.ju_kyu,
                "9-kyu" to R.string.kyu_kyu,
                "8-kyu" to R.string.hachi_kyu,
                "7-kyu" to R.string.nana_kyu,
                "6-kyu" to R.string.roku_kyu,
                "5-kyu" to R.string.go_kyu,
                "4-kyu" to R.string.yon_kyu,
                "3-kyu" to R.string.san_kyu,
                "2-kyu" to R.string.ni_kyu,
                "1-kyu" to R.string.ikk_kyu,
                "1-dan" to R.string.sho_dan,
                "2-dan" to R.string.ni_dan,
                "3-dan" to R.string.san_dan,
                "4-dan" to R.string.yon_dan,
                "5-dan" to R.string.go_dan,
                "6-dan" to R.string.roku_dan,
                "7-dan" to R.string.nana_dan,
                "8-dan" to R.string.hachi_dan
        )

        fun stringResourceFor(key: String): Int? {
            return stringToResourceMap[key]
        }

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
//                Lang.JA -> R.font.nicomoji_plus
                Lang.JA -> R.font.rounded_mplus_2p_light
                Lang.OTHERS -> R.font.rounded_mplus_2p_light
            }
            return ResourcesCompat.getFont(context, resourceId)
        }

        fun boldTypefaceForCurrentLang(context: Context): Typeface? {
            val resourceId = when (currentLang()) {
                Lang.EN -> R.font.rounded_mplus_2p_heavy
//                Lang.JA -> R.font.nicomoji_plus
                Lang.JA -> R.font.rounded_mplus_2p_heavy
                Lang.OTHERS -> R.font.rounded_mplus_2p_heavy
            }
            return ResourcesCompat.getFont(context, resourceId)
        }
    }
}