package com.example.notemanager.new_note

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.example.notemanager.R
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test

class NewNoteActivityTest {

    @get:Rule
    val activityTestRule: ActivityTestRule<NewNoteActivity> =
        ActivityTestRule(NewNoteActivity::class.java)

    @Test
    fun openNewNoteActivity_typeText_saveNote() {
        onView(withId(R.id.et_title)).perform(typeText("Hello"))

        onView(withId(R.id.et_content)).perform(typeText("World"))

        onView(withId(R.id.sp_category_list)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Home"))).perform(click())

        onView(withId(R.id.menu_save)).perform(click())

        onView(withText("Hello")).check(matches(isDisplayed()))

    }
}