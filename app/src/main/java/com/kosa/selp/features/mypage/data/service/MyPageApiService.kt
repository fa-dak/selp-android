package com.kosa.selp.features.mypage.data.service

import com.kosa.selp.features.mypage.model.GiftBundleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MyPageApiService {
    @GET("/mypage/gift-bundles")
    suspend fun getMyGiftBundles(
        @Query("sort") sort: String = "createdDate,desc"
    ): List<GiftBundleResponse>
}