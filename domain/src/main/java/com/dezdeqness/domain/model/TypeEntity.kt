package com.dezdeqness.domain.model

enum class TypeEntity(val type: String) {
    ANIME("anime"),
    MANGA("manga"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String?) = values().find { item -> item.type == value } ?: UNKNOWN
    }

}
