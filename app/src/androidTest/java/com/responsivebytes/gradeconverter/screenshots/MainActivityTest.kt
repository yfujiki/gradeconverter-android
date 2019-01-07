package com.responsivebytes.gradeconverter.screenshots

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.responsivebytes.gradeconverter.MainActivity
import com.responsivebytes.gradeconverter.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.locale.LocaleTestRule
import org.junit.ClassRule
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    companion object {
        @ClassRule
        @JvmField
        val localeTestRule = LocaleTestRule()
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

//        for (i in 0 until 4) {
//            val appCompatImageView = onView(
//                    allOf(withId(R.id.rightArrow), withContentDescription("Right arrow"),
//                            childAtPosition(
//                                    childAtPosition(
//                                            withId(R.id.recyclerView),
//                                            0),
//                                    3),
//                            isDisplayed()))
//            appCompatImageView.perform(click())
//
//            Thread.sleep(500)
//        }

        Thread.sleep(3000)

        Screengrab.screenshot("1-Main")

        val addButton = onView(withId(R.id.fab))
        addButton.perform(click())

        Thread.sleep(1000)
        Screengrab.screenshot("2-Add")

        val closeButton = onView(withId(R.id.addCloseButton))
        closeButton.perform(click())

        Thread.sleep(500)

        val editButton = onView(withId(R.id.edit_menu_item))
        editButton.perform(click())

        Thread.sleep(1000)
        Screengrab.screenshot("3-Edit")

        val closeButton2 = onView(withId(R.id.edit_menu_item))
        editButton.perform(click())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
