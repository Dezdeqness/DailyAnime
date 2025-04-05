package com.dezdeqness.domain.model

enum class InitialSection(val id: Int) {
    HOME(0),
    FAVORITES(1),
    CALENDAR(2),
    PROFILE(3),
    SEARCH(4);

    companion object {
        fun fromId(id: Int?): InitialSection = InitialSection.entries.find { it.id == id } ?: HOME
    }
}
