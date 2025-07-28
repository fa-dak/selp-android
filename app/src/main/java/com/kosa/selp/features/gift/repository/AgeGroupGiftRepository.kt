package com.kosa.selp.features.gift.repository

import com.kosa.selp.features.gift.model.AgeGroupGift
import com.kosa.selp.features.gift.service.AgeGroupGiftApiService
import jakarta.inject.Inject

class AgeGroupGiftRepository @Inject constructor(
    private val giftApiService: AgeGroupGiftApiService
) {
    suspend fun getGiftsByAgeGroup(ageGroup: String): List<AgeGroupGift> {
        return giftApiService.getGiftsByAgeGroup()
            .filter { it.ageGroup == ageGroup }
    }
}