package com.responsivebytes.gradeconverter

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.v4.view.ViewPager
import android.view.View
import org.hamcrest.Matcher

object CustomViewActions {

    fun swipeRightWithId(id: Int, distance: Float): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Swipe a child view with specified id to the right."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<ViewPager>(id)
                v.beginFakeDrag()
                v.fakeDragBy(-distance.toFloat())
                v.endFakeDrag()
            }
        }
    }

    fun swipeLeftWithId(id: Int, distance: Float): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Swipe a child view with specified id to the left."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<ViewPager>(id)
                v.beginFakeDrag()
                v.fakeDragBy(distance.toFloat())
                v.endFakeDrag()
            }
        }
    }
}