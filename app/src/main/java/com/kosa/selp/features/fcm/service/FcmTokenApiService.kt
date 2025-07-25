package com.kosa.selp.features.fcm.service

import com.kosa.selp.features.fcm.model.FcmTokenRegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmTokenApiService {
    @POST("/api/fcmToken")
    suspend fun registerToken(@Body request: FcmTokenRegisterRequestDto)
}