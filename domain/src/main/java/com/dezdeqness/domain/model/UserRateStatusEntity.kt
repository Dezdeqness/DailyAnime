package com.dezdeqness.domain.model

enum class UserRateStatusEntity(val status: String) {
    WATCHING("watching"),
    COMPLETED("completed"),
    REWATCHING("rewatching"),
    PLANNED("planned"),
    ON_HOLD("on_hold"),
    DROPPED("dropped"),
    UNKNOWN("none");

    companion object {
        fun fromString(value: String) = entries.find { item -> item.status == value } ?: UNKNOWN
    }
}
