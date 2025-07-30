package com.kosa.selp.features.gift.model

data class AgeGroupGift(
    val id: Long,
    val name: String,
    val ageGroup: String,
    val imageUrl: String,
    val price: String,
    val detailPath: String,
)