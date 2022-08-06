package com.dezdeqness.domain.model

data class FilterEntity(
    val id: String,
    val name: String,
    val type: FilterType,
)


enum class FilterType(val filterName: String) {
    STATUS("status"),
    KIND("kind"),
    SPECIAL("special"),
    DURATION("duration"),
    RATING("rating"),
    UNKNOWN("");

    companion object {
        fun fromString(value: String) = values()
            .find { it.filterName == value } ?: UNKNOWN
    }

}
