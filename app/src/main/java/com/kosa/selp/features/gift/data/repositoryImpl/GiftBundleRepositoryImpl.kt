package com.kosa.selp.features.gift.data.repositoryImpl

import com.kosa.selp.features.gift.data.remote.GiftApiService
import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleRecommendMessageResponseDto
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import javax.inject.Inject


class GiftRepositoryImpl @Inject constructor(
    private val api: GiftApiService
) : GiftRepository {

    override suspend fun getGiftBundle(giftBundleId: String): GiftBundleDetailResponseDto {
        return api.getGiftBundle(giftBundleId)
    }

    override suspend fun getRecommendedMessages(giftBundleId: String): GiftBundleRecommendMessageResponseDto {
        return api.getRecommendedMessages(giftBundleId, "감정형")
    }
}