package com.kosa.selp.features.gift.data.remote

import com.kosa.selp.features.gift.data.response.GiftBundleDetailResponseDto
import com.kosa.selp.features.gift.data.response.GiftBundleRecommendMessageResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiftApiService {

    @GET("gift-bundle/{giftBundleId}")
    suspend fun getGiftBundle(@Path("giftBundleId") giftBundleId: String): GiftBundleDetailResponseDto

    @GET("gift-bundle/{giftBundleId}/messages")
    suspend fun getRecommendedMessages(
        @Path("giftBundleId") giftBundleId: String, @Query("tone") tone: String
    ): GiftBundleRecommendMessageResponseDto
}