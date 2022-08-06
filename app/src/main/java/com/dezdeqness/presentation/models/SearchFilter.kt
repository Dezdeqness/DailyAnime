package com.dezdeqness.presentation.models

data class AnimeSearchFilter(
    val innerId: String,
    val displayName: String,
    val items: List<AnimeCell>
) : AdapterItem()

data class AnimeCell(
    val id: String,
    val displayName: String,
    val state: CellState = CellState.NONE,
)

enum class CellState {
    INCLUDE,
    EXCLUDE,
    NONE,
}

const val ANIME_SEARCH_FILTER_GENRE = "anime_search_filter_genre"
const val ANIME_SEARCH_FILTER_SEASON = "anime_search_filter_season"
const val ANIME_SEARCH_FILTER_STATUS = "anime_search_filter_status"
const val ANIME_SEARCH_FILTER_TYPE = "anime_search_filter_type"
const val ANIME_SEARCH_FILTER_TIME = "anime_search_filter_time"
const val ANIME_SEARCH_FILTER_AGE_RATING = "anime_search_filter_age_rating"