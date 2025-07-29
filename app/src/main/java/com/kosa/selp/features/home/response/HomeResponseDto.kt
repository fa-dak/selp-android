package com.kosa.selp.features.home.response

import com.kosa.selp.features.home.presentation.screen.GiftItem

data class HomeResponseDto(
    val upcomingEvents: List<UpcomingEventDto>,
    val recommendProducts: List<GiftDto>,
    val recentGiftBundleProducts: GiftBundleDto?
)

data class UpcomingEventDto(
    val id: Long,
    val eventName: String,
    val dday: Int
)

data class GiftDto(
    val id: Int,
    val name: String,
    val price: Int,
    val imagePath: String,
    val detailPath: String
)

data class GiftBundleDto(
    val giftBundleId: Long,
    val products: List<GiftDto>
)

fun GiftDto.toGiftItem(): GiftItem {
    return GiftItem(
        id = id.toString(),
        title = name,
        imageUrl = imagePath,
        price = "%,d원".format(price),
        detailPath = detailPath
    )
}