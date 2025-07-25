package com.kosa.selp.features.fcm.model

data class FcmTokenRegisterRequestDto(
    val token: String,
    val deviceType: DeviceType = DeviceType.ANDROID
)