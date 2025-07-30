package com.kosa.selp.features.mypage.model

import com.google.gson.annotations.SerializedName

data class EventDetailResponse(
    @SerializedName("eventId")
    val eventId: Long,
    @SerializedName("eventName")
    val eventName: String?,
    @SerializedName("eventType")
    val eventType: String?,
    @SerializedName("eventDate")
    val eventDate: String?,
    @SerializedName("receiverId")
    val receiverId: Long,
    @SerializedName("receiverNickname")
    val receiverNickname: String?,
    @SerializedName("notificationDaysBefore")
    val notificationDaysBefore: Int?
)
