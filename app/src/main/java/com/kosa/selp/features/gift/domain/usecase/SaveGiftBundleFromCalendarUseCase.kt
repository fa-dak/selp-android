package com.kosa.selp.features.gift.domain.usecase

import com.kosa.selp.features.gift.data.request.GiftBundleSaveFromCalendarRequestDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject

class SaveGiftBundleFromCalendarUseCase @Inject constructor(
    private val repository: GiftRepository
) {
    suspend operator fun invoke(request: GiftBundleSaveFromCalendarRequestDto) {
        repository.saveGiftBundleFromCalendar(request)
    }
}
