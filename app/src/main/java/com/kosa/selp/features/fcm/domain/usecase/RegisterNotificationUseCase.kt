package com.kosa.selp.features.fcm.domain.usecase

import com.kosa.selp.features.fcm.data.request.NotificationRequestDto
import com.kosa.selp.features.fcm.domain.repository.FcmRepository
import javax.inject.Inject

class RegisterNotificationUseCase @Inject constructor(
    private val repository: FcmRepository
) {
    suspend operator fun invoke(request: NotificationRequestDto) {
        repository.registerNotification(request)
    }
}