package com.kosa.selp.shared.data.model

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("sub")
    val userId: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String
)
