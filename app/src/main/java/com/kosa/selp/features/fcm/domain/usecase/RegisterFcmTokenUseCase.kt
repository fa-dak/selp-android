package com.kosa.selp.features.fcm.domain.usecase

import com.kosa.selp.features.fcm.data.request.FcmTokenRegisterRequestDto
import com.kosa.selp.features.fcm.domain.repository.FcmRepository
import javax.inject.Inject

class RegisterFcmTokenUseCase @Inject constructor(
    private val repository: FcmRepository
) {
    suspend operator fun invoke(request: FcmTokenRegisterRequestDto) {
        repository.registerToken(request)
    }
}