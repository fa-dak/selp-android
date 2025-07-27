package com.kosa.selp.features.fcm.data.remote

import com.kosa.selp.features.fcm.data.request.FcmTokenRegisterRequestDto
import com.kosa.selp.features.fcm.data.request.NotificationRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApiService {
    @POST("/api/fcmToken")
    suspend fun registerToken(@Body request: FcmTokenRegisterRequestDto)

    @POST("/api/notifications")
    suspend fun registerNotification(@Body request: NotificationRequestDto)

    @POST("/api/notifications/send")
    suspend fun sendNotifications()
}