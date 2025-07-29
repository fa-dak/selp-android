package com.kosa.selp.features.survey.model

enum class AnniversaryType(val label: String, val code: String) {
    ANNIVERSARY("기념일", "ANNIVERSARY"),
    WEDDING_ANNIVERSARY("결혼기념일", "WEDDING_ANNIVERSARY"),
    HOLIDAY("공휴일", "HOLIDAY"),
    MEETING("소개팅", "MEETING"),
    VACATION("휴가", "VACATION"),
    GRADUATION("졸업", "GRADUATION"),
    BIRTHDAY("생일", "BIRTHDAY"),
    ETC("기타", "ETC");


    companion object {
        fun fromLabel(label: String): AnniversaryType? =
            entries.find { it.label == label }

        fun fromCode(code: String): AnniversaryType? =
            entries.find { it.code == code }

        fun labelList(): List<String> = entries.map { it.label }
    }
}

enum class GenderType(val label: String, val code: String) {
    MALE("남성", "MALE"),
    FEMALE("여성", "FEMALE"),
    NONE("상관없음", "NONE");

    companion object {
        fun fromLabel(label: String): GenderType? =
            entries.find { it.label == label }

        fun labelList(): List<String> = entries.map { it.label }
    }
}
