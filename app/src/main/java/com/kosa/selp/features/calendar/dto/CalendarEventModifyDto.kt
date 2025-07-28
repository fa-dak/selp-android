package com.kosa.selp.features.calendar.dto

data class CalendarEventModifyDto(
    val eventDate: String,
    val eventName: String,
    val eventType: String,
    val receiverInfoId: Int,
    val notificationDaysBefore: Int,
)