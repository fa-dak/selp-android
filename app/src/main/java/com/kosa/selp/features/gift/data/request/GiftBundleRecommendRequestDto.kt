package com.kosa.selp.features.gift.data.request

import com.google.gson.annotations.SerializedName

data class GiftBundleRecommendRequestDto(
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

    @SerializedName("budget")
    val budget: Int,

    @SerializedName("userMessage")
    val userMessage: String
)