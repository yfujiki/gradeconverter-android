package com.responsivebytes.gradeconverter.screenshots

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.responsivebytes.gradeconverter.DemoModeEnabler
import com.responsivebytes.gradeconverter.MainActivity
import com.responsivebytes.gradeconverter.Models.LocalPreferencesImpl
import com.responsivebytes.gradeconverter.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.locale.LocaleTestRule
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

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        LocalPreferencesImpl(appContext!!, "UITest").reset()
    }

    @After
    fun tearDown() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        LocalPreferencesImpl(appContext!!, "UITest").reset()
    }

    @Test
    fun mainActivityTest() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

        val enabler = DemoModeEnabler()
        enabler.enable()

        Thread.sleep(3000)

        Screengrab.screenshot("1-Main")

        Thread.sleep(500)

        val addButton = onView(withId(R.id.fab))
        addButton.perform(click())

        Thread.sleep(1000)
        Screengrab.screenshot("2-Add")

        Thread.sleep(500)

        val closeButton = onView(withId(R.id.addCloseButton))
        closeButton.perform(click())

        Thread.sleep(1000)

        val editButton = onView(withId(R.id.edit_menu_item))
        editButton.perform(click())

        Thread.sleep(1000)
        Screengrab.screenshot("3-Edit")

        Thread.sleep(500)

        editButton.perform(click())

        enabler.disable()
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
