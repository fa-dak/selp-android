package com.kosa.selp.shared.composable.calendar.model

import java.util.Date

data class CalendarConfig(
    val selectedDate: Date,
    val onChange: (Date) -> Unit,
    val mode: String,
    val range: LongRange,
    val blockedTimestamps: List<Long>
)