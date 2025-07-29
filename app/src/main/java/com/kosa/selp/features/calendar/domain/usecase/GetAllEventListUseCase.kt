package com.kosa.selp.features.calendar.domain.usecase

import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.features.calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class GetAllEventListUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(year: Int, month: Int): List<EventListResponseDto> {
        return repository.getAllEventList(year, month)
    }
}