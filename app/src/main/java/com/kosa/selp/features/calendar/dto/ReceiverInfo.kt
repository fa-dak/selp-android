package com.kosa.selp.features.calendar.dto

data class ReceiverInfo(
    val receiverInfoId: Long,
    val receiverNickname: String,
    val receiverAge: Int,
    val receiverGender: String,
    val relationship: String,
    val receiverPreferences: String,
    val receiverDetail: String,
)