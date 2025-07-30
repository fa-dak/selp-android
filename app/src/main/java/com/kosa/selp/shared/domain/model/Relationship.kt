package com.kosa.selp.shared.domain.model

enum class Relationship(val displayName: String, val emoji: String) {
    PARENTS("부모님", "👨‍👩‍👧‍👦"),
    FRIEND("친구", "🧑‍🤝‍🧑"),
    LOVER("애인", "❤️"),
    COLLEAGUE("직장동료 / 상사", "💼"),
    ETC("그 외 관계", "👤");

    companion object {
        fun fromDisplayName(displayName: String): Relationship {
            return entries.find { it.displayName == displayName } ?: ETC
        }
    }
}
