package com.dezdeqness.domain.model

enum class AnimeKind(val kind: String) {
    TV("tv"),
    MOVIE("movie"),
    OVA("ova"),
    ONA("ona"),
    SPECIAL("special"),
    TV_SPECIAL("tv_special"),
    MUSIC("music"),
    PROMO("pv"),
    AD("cm"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String?) = entries.find { it.kind == value } ?: UNKNOWN
    }
}
