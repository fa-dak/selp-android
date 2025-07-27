package com.kosa.selp.features.fcm.data.request

data class NotificationRequestDto(
    val memberId: Long,
    val title: String,
    val content: String,
    val sendDate: String
)