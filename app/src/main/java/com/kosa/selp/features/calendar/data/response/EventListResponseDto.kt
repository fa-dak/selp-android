package com.kosa.selp.features.calendar.data.response

data class EventListResponseDto(
    val eventId: Long,
    val eventName: String?,
    val eventType: String?,
    val eventDate: String?,
    val receiverInfoId: Long,
    val receiverNickname: String?, // 누구한테 선물할지 만약 빈값 넘어온다면 ""
    val notificationDaysBefore: Int?
)

