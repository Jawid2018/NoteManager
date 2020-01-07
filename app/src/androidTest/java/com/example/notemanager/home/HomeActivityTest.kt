package com.example.notemanager.home

import android.text.format.DateUtils
import androidx.core.view.GravityCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.example.notemanager.R
import com.example.notemanager.adapter.NoteViewHolder
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<HomeActivity> =
        ActivityTestRule(HomeActivity::class.java)

    @Test
    fun onFabClick_displayNewNoteActivity() {
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.et_title)).check(matches(isDisplayed()))
    }

    @Test
    fun onRecycleItemClick_test_displayNewNoteActivity() {
        onView(withId(R.id.note_recycler_view)).perform(
            RecyclerViewActions.actionOnItem<NoteViewHolder>(
                hasDescendant(withText("test")), click()
            )
        )
        onView(withId(R.id.et_title)).check(matches(withText("test")))
    }

    @Test
    fun onSettingsMenuClick_displaySettingsActivity() {
        onView(withId(R.id.drawer)).perform(
            DrawerActions.open(GravityCompat.START)
        )

        onView(withId(R.id.nav_view)).perform(
            NavigationViewActions.navigateTo(R.id.menu_setting)
        )
        onView(withText("App theme")).check(matches(isDisplayed()))
        Thread.sleep(DateUtils.SECOND_IN_MILLIS * 2)
    }


}