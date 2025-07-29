package com.kosa.selp.features.calendar.data.response

data class EventListResponseDto(
    val eventId: Long,
    val eventName: String,
    val eventType: String,
    val eventDate: String,
    val receiverNickname: String,
    val notificationDaysBefore: Int?
)