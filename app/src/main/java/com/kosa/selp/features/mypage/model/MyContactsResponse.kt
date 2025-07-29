package com.kosa.selp.features.mypage.model

import com.google.gson.annotations.SerializedName

// 실제 API 응답은 이 클래스의 리스트(List<Contact>) 형태입니다.
data class Contact(
    @SerializedName("receiverInfoId")
    val id: Long,
    @SerializedName("receiverNickname")
    val nickname: String?,
    @SerializedName("receiverAge")
    val age: Int?,
    @SerializedName("receiverGender")
    val gender: String?,
    @SerializedName("relationship")
    val relationship: String?,
    @SerializedName("receiverPreferences")
    val preferences: List<String>,
    @SerializedName("receiverDetail")
    val detail: String?
)