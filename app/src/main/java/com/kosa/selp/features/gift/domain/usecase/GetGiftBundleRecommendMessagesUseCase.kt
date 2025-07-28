package com.kosa.selp.features.gift.domain.usecase

import com.kosa.selp.features.gift.data.response.GiftBundleRecommendMessageResponseDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject

class GetGiftBundleRecommendMessagesUseCase @Inject constructor(
    private val repository: GiftRepository
) {
    suspend operator fun invoke(
        giftBundleId: String,
        tone: String
    ): GiftBundleRecommendMessageResponseDto {
        return repository.getRecommendedMessages(giftBundleId, tone)
    }
}