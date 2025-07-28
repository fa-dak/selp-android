package com.kosa.selp.features.gift.domain.usecase

import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject

class RecommendGiftBundleUseCase @Inject constructor(
    private val repository: GiftRepository
) {
    suspend operator fun invoke(request: GiftBundleRecommendRequestDto): List<GiftBundleItemResponseDto> {
        return repository.recommendGiftBundle(request)
    }
}
