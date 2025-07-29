package com.kosa.selp.features.calendar.data.response

data class EventListResponseDto(
    val eventId: Long,
    val eventName: String?, // 무슨 기념일 사용자가 정하는거(입력)
    val eventType: String?, // 무슨 기념일 뭐 이런거인거같고
    val eventDate: String?, // 날짜
    val receiverNickname: String?, // 누구한테 선물할지 만약 빈값 넘어온다면 ""
    val notificationDaysBefore: Int?
)

