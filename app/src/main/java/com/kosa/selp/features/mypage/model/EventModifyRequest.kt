package com.kosa.selp.features.mypage.model

import com.google.gson.annotations.SerializedName
import com.kosa.selp.shared.domain.model.EventType

data class EventModifyRequest(
    @SerializedName("eventDate")
    val eventDate: String,
    @SerializedName("eventName")
    val eventName: String,
    @SerializedName("eventType")
    val eventType: EventType, // 타입을 String에서 EventType으로 변경
    @SerializedName("receiverInfoId")
    val receiverInfoId: Long,
    @SerializedName("notificationDaysBefore")
    val notificationDaysBefore: Int?
)
