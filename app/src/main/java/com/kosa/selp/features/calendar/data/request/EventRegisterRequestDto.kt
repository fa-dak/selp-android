package com.kosa.selp.features.calendar.composable

data class EventRegisterRequestDto(
    val eventDate: String,
    val eventName: String?,
    val eventType: AnniversaryType?,
    val receiverInfoId: Long?,
    val notificationDaysBefore: Int? = null
)