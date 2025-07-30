package com.kosa.selp.shared.domain.model

enum class EventType(val value: String) {
    BIRTHDAY("생일"),
    MEETING("미팅"),
    VACATION("방학"),
    ANNIVERSARY("기념일"),
    WEDDING_ANNIVERSARY("결혼 기념일"),
    GRADUATION("졸업"),
    HOLIDAY("휴일"),
    ETC("기타");

    companion object {
        fun fromValue(value: String?): EventType? {
            return entries.find { it.value == value }
        }

        fun fromName(name: String?): EventType? {
            return entries.find { it.name == name }
        }
    }
}
