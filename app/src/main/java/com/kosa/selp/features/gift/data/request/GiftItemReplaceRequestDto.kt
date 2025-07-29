package com.kosa.selp.features.gift.data.request

import com.google.gson.annotations.SerializedName

data class GiftItemReplaceRequestDto(
    @SerializedName("productId")
    val productId: Long,

    @SerializedName("ageRange")
    val ageRange: Int?,

    @SerializedName("anniversaryType")
    val anniversaryType: String?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("relation")
    val relation: String?,

    @SerializedName("gender")
    val gender: String?,

    @SerializedName("price")
    val price: Long,

    @SerializedName("userMessage")
    val userMessage: String?
)