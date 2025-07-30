package com.kosa.selp.features.calendar.domain.usecase

import EventRegisterRequestDto
import com.kosa.selp.features.calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class RegisterEventUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(eventRegisterRequest: EventRegisterRequestDto) {
        repository.registerEvent(eventRegisterRequest)
    }
}