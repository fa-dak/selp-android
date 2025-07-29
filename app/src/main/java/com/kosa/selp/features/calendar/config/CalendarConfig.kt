package com.kosa.selp.features.calendar.config


import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import java.util.Date

data class CalendarConfig(
    val selectedDate: Date,
    val onChange: (Date) -> Unit,
    val events: List<EventListResponseDto>
)