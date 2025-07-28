package com.kosa.selp.features.gift.domain.usecase

import com.kosa.selp.features.gift.data.request.GiftItemReplaceRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject

class ReplaceGiftItemUseCase @Inject constructor(
    private val repository: GiftRepository
) {
    suspend operator fun invoke(giftItemReplaceRequest: GiftItemReplaceRequestDto): GiftBundleItemResponseDto {
        return repository.replaceGiftItem(giftItemReplaceRequest)
    }
}
