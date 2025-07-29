package com.kosa.selp.features.notification.service

import com.kosa.selp.features.notification.data.response.NotificationFindResponseDto
import retrofit2.http.GET

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(): List<NotificationFindResponseDto>
}