package com.kosa.selp.features.gift.data.response

data class GiftBundleDetailResponseDto(
    val giftBundleId: Long,
    val createdDate: String,
    val eventId: Long,
    val eventType: String,
    val eventDate: String,
    val eventName: String?,
    val receiverInfoId: Long,
    val receiverNickname: String?,
    val relationship: String,
    val products: List<GiftItemDto>
)

data class GiftItemDto(
    val productId: Long,
    val category: String?, // 혹은 enum이면 enum으로
    val name: String,
    val price: Int,
    val imagePath: String,
    val detailPath: String
)