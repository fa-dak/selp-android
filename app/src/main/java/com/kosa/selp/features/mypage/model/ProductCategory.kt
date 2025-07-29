package com.kosa.selp.features.mypage.model

import com.google.gson.annotations.SerializedName

data class ProductCategory(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameKR")
    val nameKR: String
)
