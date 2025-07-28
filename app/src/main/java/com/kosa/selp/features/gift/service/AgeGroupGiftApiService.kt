package com.kosa.selp.features.gift.service

import com.kosa.selp.features.gift.model.AgeGroupGift
import retrofit2.http.GET

interface AgeGroupGiftApiService {
    @GET("/api/agerecommend")
    suspend fun getGiftsByAgeGroup(): List<AgeGroupGift>
}