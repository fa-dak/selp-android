package com.kosa.selp.features.gift.data.request

data class GiftBundleSaveFromCalendarRequestDto(
    val giftIds: List<Long>,
    val eventId: Long
)