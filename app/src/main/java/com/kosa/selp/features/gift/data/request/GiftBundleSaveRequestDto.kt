package com.kosa.selp.features.gift.data.request

import com.google.gson.annotations.SerializedName

data class GiftBundleSaveRequestDto(
    @SerializedName("giftIds")
    val giftIds: List<Long>,

    @SerializedName("ageRange")
    val ageRange: Int,

    @SerializedName("anniversaryType")
    val anniversaryType: String,

    @SerializedName("categories")
    val categories: List<String>,

    @SerializedName("relation")
    val relation: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("detail")
    val detail: String
)