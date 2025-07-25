package com.kosa.selp.features.home.ageGroupGift.repository

import com.kosa.selp.features.home.ageGroupGift.model.AgeGroupGiftItem

class AgeGroupGiftRepository {
    fun getGiftsByAgeGroup(ageGroup: String): List<AgeGroupGiftItem> {
        // 실제는 API 호출 or DB 연동
        return dummyGifts.filter { it.ageGroup == ageGroup }
    }

    private val dummyGifts = listOf(
        AgeGroupGiftItem(
            "향 좋은 디퓨저",
            "10대",
            "https://ldb-phinf.pstatic.net/20241226_124/1735176183750fjEhg_JPEG/%C8%A6%C5%D7%B5%F0%C4%C9%C0%CC%C5%A9_%281%29.jpg",
            "19,800원"
        ),
        AgeGroupGiftItem(
            "레터링 케이크",
            "10대",
            "https://ldb-phinf.pstatic.net/20241226_81/1735176192690m2B0T_JPEG/%C8%AD%C0%CC%C6%AE%C8%A6%C5%D7%B5%F0.jpeg",
            "35,000원"
        ),
        AgeGroupGiftItem(
            "무드등",
            "10대",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "28,900원"
        ),
        AgeGroupGiftItem(
            "감성 꽃다발",
            "10대",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "42,000원"
        ),
        AgeGroupGiftItem(
            "에어팟 케이스",
            "20대",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "12,500원"
        ),
        AgeGroupGiftItem(
            "감성 엽서 세트",
            "30대",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "8,900원"
        ),
    )
}