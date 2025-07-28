package com.kosa.selp.features.calendar.dto

import java.util.Date

data class CalendarEvent(
    val eventId: String,
    val eventName: String,
    val eventType: String,
    val receiverNickname: String,
    val notificationDaysBefore: Int,
    val eventDate: Date
)