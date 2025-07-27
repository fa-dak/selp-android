package com.kosa.selp.features.fcm.data.request

import com.kosa.selp.features.fcm.model.DeviceType

data class FcmTokenRegisterRequestDto(
    val token: String,
    val deviceType: DeviceType = DeviceType.ANDROID
)