package com.kosa.selp.features.gift.repository

import com.kosa.selp.features.gift.model.Gift

object GiftRepository {
    fun getRecommendedGifts(): List<Gift> = listOf(
        Gift(
            id = 1,
            name = "나이키 에어포스 1 로우 올백 트리플화이트",
            imageUrl = "https://picsum.photos/300/300?random=1",
            description = "남녀공용 스니커즈",
            price = 129000
        ),
        Gift(
            id = 2,
            name = "Apple 에어팟 프로 블루투스 이어폰 정품",
            imageUrl = "https://picsum.photos/300/300?random=1",
            description = "노이즈 캔슬링",
            price = 211500
        ),
        Gift(
            id = 3,
            name = "존바바토스 아티산 EDT 75ml 남자향수",
            imageUrl = "https://picsum.photos/300/300?random=1",
            description = "가볍고 향기로운 남성 향수",
            price = 65600
        ),
        Gift(
            id = 4,
            name = "휴고보스 남성 메탈시계 1513597",
            imageUrl = "https://picsum.photos/300/300?random=1",
            description = "메탈 밴드, 블루 다이얼",
            price = 618000
        )
    )
}
