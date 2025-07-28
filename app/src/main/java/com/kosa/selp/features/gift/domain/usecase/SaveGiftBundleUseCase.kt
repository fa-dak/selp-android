package com.kosa.selp.features.gift.domain.usecase

import com.kosa.selp.features.gift.data.request.GiftBundleSaveRequestDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject

class SaveGiftBundleUseCase @Inject constructor(
    private val repository: GiftRepository
) {
    suspend operator fun invoke(giftBundleSaveRequestDto: GiftBundleSaveRequestDto) {
        return repository.saveGiftBundle(giftBundleSaveRequestDto)
    }
}
