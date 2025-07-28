package com.kosa.selp.features.mypage.data.repository

import com.kosa.selp.features.mypage.data.service.MyPageApiService
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val myPageApiService: MyPageApiService
) {
    suspend fun getMyGiftBundles() = myPageApiService.getMyGiftBundles()

    suspend fun getMyGiftBundleDetail(bundleId: Long) = myPageApiService.getMyGiftBundleDetail(bundleId)

    suspend fun getMyReceiverInfoList() = myPageApiService.getMyReceiverInfoList()

    suspend fun getMyReceiverInfoDetail(receiverInfoId: Long) = myPageApiService.getMyReceiverInfoDetail(receiverInfoId)
}
