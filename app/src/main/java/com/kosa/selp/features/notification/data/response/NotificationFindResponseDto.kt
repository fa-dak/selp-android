package com.kosa.selp.features.notification.data.response

data class NotificationFindResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val createdDate: String
)