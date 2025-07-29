package com.kosa.selp.features.mypage.model

import kotlinx.serialization.Serializable

@Serializable
data class ReceiverModifyRequest(
    val nickname: String,
    val gender: String,
    val relationship: String,
    val age: Int,
    val preferences: String,
    val detail: String
)
