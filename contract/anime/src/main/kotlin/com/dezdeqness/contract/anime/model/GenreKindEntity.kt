package com.dezdeqness.contract.anime.model

enum class GenreKindEntity(val kind: String) {
    GENRE("genre"),
    THEME("theme"),
    DEMOGRAPHIC("demographic"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String?) = GenreKindEntity.entries
            .find { item -> item.kind.contentEquals(value, true) }
            ?: UNKNOWN
    }
}
