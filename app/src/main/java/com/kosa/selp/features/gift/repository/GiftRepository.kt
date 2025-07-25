package com.kosa.selp.features.gift.repository

import com.kosa.selp.features.gift.model.Gift

object GiftRepository {
    fun getRecommendedGifts(): List<Gift> = listOf(
        Gift(
            id = 1,
            name = "나이키 에어포스 1 로우 올백 트리플화이트",
            imageUrl = "https://ldb-phinf.pstatic.net/20241226_224/17351761367157tABp_JPEG/%BA%A3%C0%CC%BA%F1%C5%D7%B5%F0%C4%C9%C0%CC%C5%A9_%281%29.jpg",
            description = "남녀공용 스니커즈",
            price = 129000
        ),
        Gift(
            id = 2,
            name = "Apple 에어팟 프로 블루투스 이어폰 정품",
            imageUrl = "https://ldb-phinf.pstatic.net/20241226_24/1735176152776gWdXs_JPEG/%B0%ED%BF%B5%C8%F1%BE%F3%B1%BC%C4%C9%C0%CC%C5%A9.jpg",
            description = "노이즈 캔슬링",
            price = 211500
        ),
        Gift(
            id = 3,
            name = "존바바토스 아티산 EDT 75ml 남자향수",
            imageUrl = "https://ldb-phinf.pstatic.net/20250324_137/1742780003785n3HIU_JPEG/250317_%BE%F3%B1%D7%B7%B9%C0%CC%C6%DB%C7%C7%28%B9%CC%B4%CF%29.jpg",
            description = "가볍고 향기로운 남성 향수",
            price = 65600
        ),
        Gift(
            id = 4,
            name = "휴고보스 남성 메탈시계 1513597",
            imageUrl = "https://ldb-phinf.pstatic.net/20241226_207/1735176174670oG8AL_JPEG/%B7%B9%B5%E5%BA%A7%BA%AA%C6%DB%C7%C7%C8%A6%C4%C9%C0%CC%C5%A9_%281%29.jpeg",
            description = "메탈 밴드, 블루 다이얼",
            price = 618000
        )
    )
}
