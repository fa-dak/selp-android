package com.kosa.selp.features.gift.domain.repository

import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleRecommendMessageResponseDto

interface GiftRepository {
    suspend fun getGiftBundle(giftBundleId: String): GiftBundleDetailResponseDto
    suspend fun getRecommendedMessages(giftBundleId: String): GiftBundleRecommendMessageResponseDto
    suspend fun recommendGiftBundle(giftBundleRecommendRequest: GiftBundleRecommendRequestDto): List<GiftBundleItemResponseDto>
}