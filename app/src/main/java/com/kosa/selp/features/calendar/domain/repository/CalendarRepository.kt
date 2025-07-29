package com.kosa.selp.features.calendar.domain.repository

import com.kosa.selp.features.calendar.data.response.EventListResponseDto

interface CalendarRepository {
    suspend fun getAllEventList(year: Int, month: Int): List<EventListResponseDto>
}

