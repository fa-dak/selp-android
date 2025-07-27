package com.kosa.selp.features.fcm.domain.repository

import com.kosa.selp.features.fcm.data.request.FcmTokenRegisterRequestDto
import com.kosa.selp.features.fcm.data.request.NotificationRequestDto

interface FcmRepository {
    suspend fun registerToken(request: FcmTokenRegisterRequestDto)
    suspend fun registerNotification(request: NotificationRequestDto)
    suspend fun sendNotifications()
}