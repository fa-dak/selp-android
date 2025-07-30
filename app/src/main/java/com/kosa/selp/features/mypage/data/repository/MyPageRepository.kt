package com.kosa.selp.features.mypage.data.repository

import com.kosa.selp.features.mypage.data.service.MyPageApiService
import com.kosa.selp.features.mypage.model.EventModifyRequest
import javax.inject.Inject

import com.kosa.selp.features.mypage.model.ReceiverRegisterRequest
import com.kosa.selp.features.mypage.model.ReceiverModifyRequest

class MyPageRepository @Inject constructor(
    private val myPageApiService: MyPageApiService
) {
    suspend fun getMyGiftBundles() = myPageApiService.getMyGiftBundles()

    suspend fun getMyGiftBundleDetail(bundleId: Long) = myPageApiService.getMyGiftBundleDetail(bundleId)

    suspend fun getMyReceiverInfoList() = myPageApiService.getMyReceiverInfoList()

    suspend fun getMyReceiverInfoDetail(receiverInfoId: Long) = myPageApiService.getMyReceiverInfoDetail(receiverInfoId)

    suspend fun modifyReceiverInfo(receiverInfoId: Long, request: ReceiverModifyRequest) = myPageApiService.modifyReceiverInfo(receiverInfoId, request)

    suspend fun deleteReceiverInfo(receiverInfoId: Long) = myPageApiService.deleteReceiverInfo(receiverInfoId)

    suspend fun registerReceiverInfo(request: ReceiverRegisterRequest) = myPageApiService.registerReceiverInfo(request)

    suspend fun getProductCategories() = myPageApiService.getProductCategories()

    suspend fun getEventDetail(eventId: Long) = myPageApiService.getEventDetail(eventId)

    suspend fun modifyEvent(eventId: Long, request: EventModifyRequest) = myPageApiService.modifyEvent(eventId, request)
}
