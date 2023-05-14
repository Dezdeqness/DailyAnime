package com.dezdeqness.domain.model

enum class AnimeKind(val kind: String) {
    TV("tv"),
    MOVIE("movie"),
    OVA("ova"),
    ONA("ona"),
    SPECIAL("special"),
    MUSIC("music"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String?) = values().find { it.kind == value } ?: UNKNOWN
    }
}
