package com.dezdeqness.domain.model

data class FilterEntity(
    val id: String,
    val name: String,
    val type: FilterType,
)

enum class FilterType(val filterName: String) {
    GENRE("genre"),
    THEME("theme"),
    AUDIENCE("audience"),
    STATUS("status"),
    KIND("kind"),
    SEASON("season"),
    DURATION("duration"),
    RATING("rating"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String) = entries
            .find { it.filterName == value } ?: UNKNOWN
    }

}
