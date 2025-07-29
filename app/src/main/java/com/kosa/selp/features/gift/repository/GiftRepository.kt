package com.kosa.selp.features.gift.repository

import com.kosa.selp.features.gift.data.remote.GiftApiService
import com.kosa.selp.features.gift.data.response.GiftInfoResponseDto
import jakarta.inject.Inject

class GiftRepository @Inject constructor(
    private val api: GiftApiService
) {
    suspend fun getGiftInfo(giftId: Long): GiftInfoResponseDto {
        return api.getGiftInfo(giftId)
    }

}
