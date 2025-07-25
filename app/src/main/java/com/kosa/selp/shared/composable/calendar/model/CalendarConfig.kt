package com.kosa.selp.shared.composable.calendar.model

import java.util.Date

// 날짜별 이벤트 정보 데이터 클래스
data class CalendarEvent(
    val eventId: String,
    val eventName: String,
    val eventType: String,
    val receiverNickname: String,
    val notificationDaysBefore: Int,
    val eventDate: java.util.Date // 날짜 필드 추가
)

data class CalendarConfig(
    val selectedDate: Date,
    val onChange: (Date) -> Unit,
    val mode: String,
    val range: LongRange,
    val blockedTimestamps: List<Long>,
    val events: List<CalendarEvent> // 날짜별 이벤트 리스트
)