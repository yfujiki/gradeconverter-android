package com.responsivebytes.gradeconverter.main

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.responsivebytes.gradeconverter.*
import com.responsivebytes.gradeconverter.Models.LocalPreferencesImpl
import com.responsivebytes.gradeconverter.Views.AddRecyclerViewHolder
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.runner.RunWith
import tools.fastlane.screengrab.locale.LocaleTestRule
import android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import android.view.WindowManager
import netscape.javascript.JSObject.getWindow



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

    var appContext: Context? = null

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        LocalPreferencesImpl(appContext!!, "UITest").reset()

        unlockScreen()
    }

    private fun unlockScreen() {
        val activity = mActivityTestRule.getActivity()
        val wakeUpDevice = Runnable {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        activity.runOnUiThread(wakeUpDevice)
    }

    @After
    fun tearDown() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        LocalPreferencesImpl(appContext!!, "UITest").reset()
    }

    @Test
    fun onlyOneBaseSystemTest() {
        val moreButton = onView(withId(R.id.fab))
        moreButton.perform(click())

        val gradeSystemsToAdd = mActivityTestRule.activity.localPreferences.unselectedGradeSystems().count()
        for (i in 0..gradeSystemsToAdd - 1) {
            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<AddRecyclerViewHolder>(0, click()))
        }

        // Scroll to get base system
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition<AddRecyclerViewHolder>(0, CustomViewActions.swipeRightWithId(R.id.viewPager, 320.toFloat())))

        // First item has border
        onView(withRecyclerView(R.id.recyclerView).atPosition(0))
                .check(
                ViewAssertions.matches(
                        CustomViewMatchers.backgroundHasBorder()
                ))

        // From there on, the border should be blank
        for (i in 1..LocalPreferencesImpl.DEFAULT_GRADE_SYSTEM.count() + gradeSystemsToAdd - 1) {
            onView(withId(R.id.recyclerView))
                    .perform(scrollToPosition<AddRecyclerViewHolder>(i))

            onView(withRecyclerView(R.id.recyclerView).atPosition(i))
                    .check(
                            ViewAssertions.matches(
                                    not (
                                            CustomViewMatchers.backgroundHasBorder()
                                    )
                            )
                    )
        }
    }

    @Test
    fun addButtonShouldBeDisabledWhenNothingToAdd() {
        val moreButton = onView(withId(R.id.fab))
        moreButton.perform(click())

        while (mActivityTestRule.activity.localPreferences.unselectedGradeSystems().count() > 0) {
            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<AddRecyclerViewHolder>(0, click()))
        }

        Thread.sleep(500)

        moreButton.check(matches(not(isDisplayed())))
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
