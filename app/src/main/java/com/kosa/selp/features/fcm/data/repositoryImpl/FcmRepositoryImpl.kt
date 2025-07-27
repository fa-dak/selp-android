package com.kosa.selp.features.fcm.data.repositoryImpl

import com.kosa.selp.features.fcm.data.remote.FcmApiService
import com.kosa.selp.features.fcm.data.request.FcmTokenRegisterRequestDto
import com.kosa.selp.features.fcm.data.request.NotificationRequestDto
import com.kosa.selp.features.fcm.domain.repository.FcmRepository
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val api: FcmApiService
) : FcmRepository {

    override suspend fun registerToken(request: FcmTokenRegisterRequestDto) {
        api.registerToken(request)
    }

    override suspend fun registerNotification(request: NotificationRequestDto) {
        api.registerNotification(request)
    }

    override suspend fun sendNotifications() {
        api.sendNotifications()
    }
}