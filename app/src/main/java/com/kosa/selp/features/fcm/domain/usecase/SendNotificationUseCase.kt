package com.kosa.selp.features.fcm.domain.usecase

import com.kosa.selp.features.fcm.domain.repository.FcmRepository
import javax.inject.Inject

class SendNotificationsUseCase @Inject constructor(
    private val repository: FcmRepository
) {
    suspend operator fun invoke() {
        repository.sendNotifications()
    }
}