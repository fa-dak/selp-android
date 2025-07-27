package com.kosa.selp.features.gift.domain.usecase

import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject

class GetGiftBundleDetailUseCase @Inject constructor(
    private val repository: GiftRepository
) {
    suspend operator fun invoke(giftBundleId: String): GiftBundleDetailResponseDto {
        return repository.getGiftBundle(giftBundleId)
    }
}
