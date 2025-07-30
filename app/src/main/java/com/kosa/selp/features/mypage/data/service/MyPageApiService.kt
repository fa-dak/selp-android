package com.kosa.selp.features.mypage.data.service

import com.kosa.selp.features.mypage.model.Contact
import com.kosa.selp.features.mypage.model.EventDetailResponse
import com.kosa.selp.features.mypage.model.EventModifyRequest
import com.kosa.selp.features.mypage.model.GiftBundleResponse
import com.kosa.selp.features.mypage.model.ReceiverModifyRequest
import com.kosa.selp.features.mypage.model.ReceiverRegisterRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST

import com.kosa.selp.features.mypage.model.ProductCategory

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

    @PUT("/receiver-infos/{receiver-info-id}")
    suspend fun modifyReceiverInfo(
        @Path("receiver-info-id") receiverInfoId: Long,
        @Body request: ReceiverModifyRequest
    )

    @DELETE("/receiver-infos/{receiver-info-id}")
    suspend fun deleteReceiverInfo(
        @Path("receiver-info-id") receiverInfoId: Long
    )

    @POST("/receiver-infos")
    suspend fun registerReceiverInfo(
        @Body request: ReceiverRegisterRequest
    )

    @GET("/productCategories")
    suspend fun getProductCategories(): List<ProductCategory>

    @GET("/events/{event-id}")
    suspend fun getEventDetail(
        @Path("event-id") eventId: Long
    ): EventDetailResponse

    @PUT("/events/{event-id}")
    suspend fun modifyEvent(
        @Path("event-id") eventId: Long,
        @Body request: EventModifyRequest
    )
}
