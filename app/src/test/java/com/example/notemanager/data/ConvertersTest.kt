package com.example.notemanager.data

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class ConvertersTest {
    private lateinit var calendar: Calendar

    @Before
    fun setup() {
        calendar = Calendar.getInstance()
    }

    @Test
    fun testDateStampToCalendar() {
        val time = Converters().calendarToDateStamp(calendar)
        assertThat(time, `is`(calendar.timeInMillis))
    }

    @Test
    fun testCalendarToDateStamp() {
        val value = Converters().dateStampToCalendar(calendar.timeInMillis)
        assertThat(value, `is`(calendar))
    }
}