package com.kosa.selp.features.mypage.data.service

import com.kosa.selp.features.mypage.model.Contact
import com.kosa.selp.features.mypage.model.GiftBundleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPageApiService {
    @GET("/gift-bundle/gift-bundles")
    suspend fun getMyGiftBundles(
        @Query("sort") sort: String = "createdDate,desc"
    ): List<GiftBundleResponse>

    @GET("/gift-bundle/gift-bundles/{bundleId}")
    suspend fun getMyGiftBundleDetail(
        @Path("bundleId") bundleId: Long
    ): GiftBundleResponse

    @GET("/receiver-infos")
    suspend fun getMyReceiverInfoList(): List<Contact>

    @GET("/receiver-infos/{receiver-info-id}")
    suspend fun getMyReceiverInfoDetail(
        @Path("receiver-info-id") receiverInfoId: Long
    ): Contact
}
