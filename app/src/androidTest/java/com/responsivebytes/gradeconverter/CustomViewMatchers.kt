package com.responsivebytes.gradeconverter

import android.graphics.drawable.GradientDrawable
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object CustomViewMatchers {
    fun backgroundHasBorder(): Matcher<View> {
        return object : TypeSafeMatcher<View>(View::class.java!!) {

            override fun describeTo(description: Description) {
                description.appendText("to have a border")
            }

            override fun matchesSafely(foundView: View): Boolean {
                val gradientDrawable = (foundView.background as? GradientDrawable)
                if (gradientDrawable == null) {
                    print("Background is not a gradient")
                    return false
                }

                val field = (GradientDrawable::class.java.getDeclaredField("mStrokePaint"))
                field.isAccessible = true
                val stroke = field.get(gradientDrawable)
                return stroke != null
            }
        }
    }
}