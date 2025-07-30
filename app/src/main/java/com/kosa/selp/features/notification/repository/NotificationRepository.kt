package com.kosa.selp.features.notification.repository

import com.kosa.selp.features.notification.data.response.NotificationFindResponseDto
import com.kosa.selp.features.notification.service.NotificationApi
import jakarta.inject.Inject

class NotificationRepository @Inject constructor(
    private val api: NotificationApi
) {
    suspend fun getNotifications(): List<NotificationFindResponseDto> {
        return api.getNotifications()
    }

    suspend fun getUnreadCount(): Int {
        return api.getUnreadCount()
    }
}