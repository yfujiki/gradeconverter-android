package com.responsivebytes.gradeconverter

import android.graphics.drawable.GradientDrawable
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomViewMatchers {
    fun backgroundHasBorder(): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java!!) {

            override fun describeTo(description: Description) {
                description.appendText("to have a border")
            }

            override fun describeMismatch(item: Any?, description: Description?) {
                super.describeMismatch(item, description)

                val foundView = item as? View
                if (foundView == null) {
                    description?.appendText("Target is not a view")
                }

                val gradientDrawable = foundView?.background as? GradientDrawable
                if (gradientDrawable == null) {
                    description?.appendText("Background is not a shape")
                }
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