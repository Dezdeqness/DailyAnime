package com.dezdeqness.domain.model

data class PersonalListFilterEntity(
    val isAscending: Boolean,
    val sort: Sort,
) {
    companion object {
        fun createFilter(isAscending: Boolean, sort: String): PersonalListFilterEntity {
            return PersonalListFilterEntity(
                isAscending = isAscending,
                sort = Sort.fromString(sort),
            )
        }
    }
}

enum class Sort(val sort: String) {
    NAME("name"),
    PROGRESS("progress"),
    SCORE("score"),
    EPISODES("episodes");
    // TODO: Refactor time saving
//    ADDED_TIME("added_time"),
//    UPDATED_TIME("updated_time");

    companion object {
        fun fromString(value: String) = Sort.values().find { it.sort == value } ?: NAME
    }
}
