package com.yfujiki.gradeconverter.Views

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.widget.TextView
import com.yfujiki.gradeconverter.Utilities.Localization

class GCTextView: TextView {
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int):
            super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun init() {
        val typeface = Localization.typefaceForCurrentLang(context)
        val boldTypeface = Localization.boldTypefaceForCurrentLang(context)
        setTypeface(typeface, Typeface.NORMAL)
        setTypeface(boldTypeface, Typeface.BOLD)
    }
}
