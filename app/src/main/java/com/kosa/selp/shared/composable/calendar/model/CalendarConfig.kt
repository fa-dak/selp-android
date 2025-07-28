package com.kosa.selp.shared.composable.calendar.model

import com.kosa.selp.features.calendar.dto.CalendarEvent
import java.util.Date


data class CalendarConfig(
    val selectedDate: Date,
    val onChange: (Date) -> Unit,
    val mode: String,
    val range: LongRange,
    val blockedTimestamps: List<Long>,
    val events: List<CalendarEvent> // 날짜별 이벤트 리스트
)