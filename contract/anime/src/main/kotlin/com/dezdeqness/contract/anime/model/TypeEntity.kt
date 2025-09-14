package com.dezdeqness.contract.anime.model

enum class TypeEntity(val type: String) {
    ANIME("anime"),
    MANGA("manga"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String?) = entries
            .find { item -> item.type.contentEquals(value, true) }
            ?: UNKNOWN
    }

}
