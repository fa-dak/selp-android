package com.kosa.selp.features.mypage.data.repository

import com.kosa.selp.features.mypage.data.service.MyPageApiService
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val myPageApiService: MyPageApiService
) {
    suspend fun getMyGiftBundles() = myPageApiService.getMyGiftBundles()
}
