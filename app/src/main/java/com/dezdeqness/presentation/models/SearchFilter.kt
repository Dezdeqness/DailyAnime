package com.dezdeqness.presentation.models

data class AnimeSearchFilter(
    val innerId: String,
    val displayName: String,
    val items: List<AnimeCell>
)

data class AnimeCell(
    val id: String,
    val displayName: String,
    var state: CellState = CellState.NONE,
)

enum class CellState {
    INCLUDE,
    EXCLUDE,
    NONE,
}
