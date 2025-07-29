package com.kosa.selp.features.calendar.utils

import java.util.Calendar
import java.util.Date

object DateUtils {

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun getTomorrow(date: Date): Date {
        val calendar = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.time
    }

    fun getYesterday(date: Date): Date {
        val calendar = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_MONTH, -1)
        }
        return calendar.time
    }

    fun getDayStart(date: Date): Date {
        return Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    fun getDayEnd(date: Date): Date {
        return Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time
    }
}