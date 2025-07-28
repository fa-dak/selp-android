package com.kosa.selp.features.gift.data.response

import com.google.gson.annotations.SerializedName

data class GiftBundleItemResponseDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("price")
    val price: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("imagePath")
    val imagePath: String,

    @SerializedName("detailPath")
    val detailPath: String,

    @SerializedName("category")
    val category: String
)