package com.responsivebytes.gradeconverter.Views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.support.v7.widget.AppCompatTextView
import com.responsivebytes.gradeconverter.Utilities.Localization

class GCTextView : AppCompatTextView {
    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):
            super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        val typeface = Localization.typefaceForCurrentLang(context)
        val boldTypeface = Localization.boldTypefaceForCurrentLang(context)
        setTypeface(typeface, Typeface.NORMAL)
        setTypeface(boldTypeface, Typeface.BOLD)
    }
}
