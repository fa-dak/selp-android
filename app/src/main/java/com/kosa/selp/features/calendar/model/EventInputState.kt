package com.kosa.selp.features.calendar.model

import EventRegisterRequestDto
import com.kosa.selp.features.survey.model.AnniversaryType

data class EventInputState(
    val eventName: String = "",
    val eventType: AnniversaryType? = null,
    val receiverInfoId: Long? = null,
    val notificationDaysBefore: Int? = null
)

fun EventInputState.toDto(selectedDate: String): EventRegisterRequestDto {
    return EventRegisterRequestDto(
        eventDate = selectedDate,
        eventName = this.eventName,
        eventType = this.eventType,
        receiverInfoId = this.receiverInfoId,
        notificationDaysBefore = this.notificationDaysBefore
    )
}