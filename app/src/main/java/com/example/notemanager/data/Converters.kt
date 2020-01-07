package com.example.notemanager.data

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun dateStampToCalendar(date: Long?): Calendar? {
        date?.let {
            return Calendar.getInstance().apply {
                timeInMillis = date
            }
        } ?: return null
    }

    @TypeConverter
    fun calendarToDateStamp(calendar: Calendar?): Long? =
        calendar?.timeInMillis
}