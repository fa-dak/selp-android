package com.kosa.selp.features.calendar.data.repositoryImpl

import EventRegisterRequestDto
import com.kosa.selp.features.calendar.data.remote.CalendarApiService
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.features.calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api: CalendarApiService
) : CalendarRepository {

    override suspend fun getAllEventList(year: Int, month: Int): List<EventListResponseDto> {
        return api.getAllEventList(year, month)
    }

    override suspend fun registerEvent(eventRegisterRequest: EventRegisterRequestDto) {
        return api.registerEvent(eventRegisterRequest)
    }
}

