package com.kosa.selp.features.gift.domain.repository

import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.data.request.GiftBundleSaveRequestDto
import com.kosa.selp.features.gift.data.request.GiftItemReplaceRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleRecommendMessageResponseDto

interface GiftRepository {
    suspend fun getGiftBundle(giftBundleId: String): GiftBundleDetailResponseDto
    suspend fun getRecommendedMessages(
        giftBundleId: String,
        tone: String
    ): GiftBundleRecommendMessageResponseDto

    suspend fun recommendGiftBundle(giftBundleRecommendRequest: GiftBundleRecommendRequestDto): List<GiftBundleItemResponseDto>
    suspend fun replaceGiftItem(giftItemReplaceRequest: GiftItemReplaceRequestDto): GiftBundleItemResponseDto

    suspend fun saveGiftBundle(giftBundleSaveRequest: GiftBundleSaveRequestDto)
}