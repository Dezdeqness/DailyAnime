package com.dezdeqness.domain.model

enum class AnimeStatus(val status: String) {
    ANONS("anons"),
    ONGOING("ongoing"),
    RELEASED("released"),
    LATEST("latest"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String) = values().find { it.status == value } ?: UNKNOWN
    }

}
