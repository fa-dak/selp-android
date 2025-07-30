package com.kosa.selp.features.gift.data.repositoryImpl

import com.kosa.selp.features.gift.data.remote.GiftApiService
import com.kosa.selp.features.gift.data.request.GiftBundleRecommendRequestDto
import com.kosa.selp.features.gift.data.request.GiftBundleSaveFromCalendarRequestDto
import com.kosa.selp.features.gift.data.request.GiftBundleSaveRequestDto
import com.kosa.selp.features.gift.data.request.GiftItemReplaceRequestDto
import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleItemResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleRecommendMessageResponseDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject


class GiftRepositoryImpl @Inject constructor(
    private val api: GiftApiService
) : GiftRepository {

    override suspend fun getGiftBundle(giftBundleId: String): GiftBundleDetailResponseDto {
        return api.getGiftBundle(giftBundleId)
    }

    override suspend fun getRecommendedMessages(
        giftBundleId: String,
        tone: String
    ): GiftBundleRecommendMessageResponseDto {
        return api.getRecommendedMessages(giftBundleId, tone)
    }

    override suspend fun recommendGiftBundle(giftBundleRecommendRequest: GiftBundleRecommendRequestDto): List<GiftBundleItemResponseDto> {
        return api.recommendGiftBundle(giftBundleRecommendRequest)
    }

    override suspend fun replaceGiftItem(giftItemReplaceRequest: GiftItemReplaceRequestDto): GiftBundleItemResponseDto {
        return api.replaceGiftItem(giftItemReplaceRequest)
    }

    override suspend fun saveGiftBundle(giftBundleSaveRequest: GiftBundleSaveRequestDto) {
        api.saveGiftBundle(giftBundleSaveRequest)
    }

    override suspend fun saveGiftBundleFromCalendar(giftBundleSaveFromCalendarRequest: GiftBundleSaveFromCalendarRequestDto) {
        api.saveGiftBundleFromCalendar(giftBundleSaveFromCalendarRequest)
    }
}