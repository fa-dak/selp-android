package com.kosa.selp.features.gift.data.response

data class GiftBundleDetailResponseDto(
    val userName: String,
    val ageGroup: String,
    val relation: String,
    val giftList: List<GiftItemDto>
)

data class GiftItemDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: String
)