package com.kosa.selp.features.calendar.dto

data class CalendarEventRegisterDto(
    val eventDate: String,
    val eventName: String,
    val eventType: String,
    val receiverInfoId: Long,
    val notificationDaysBefore: Int,
)