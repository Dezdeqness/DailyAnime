package com.dezdeqness.presentation.features.animelist

import androidx.compose.runtime.Immutable
import com.dezdeqness.presentation.models.SearchSectionUiModel

@Immutable
data class AnimeSearchState(
    val list: List<AnimeUiModel> = listOf(),
    val status: AnimeSearchStatus = AnimeSearchStatus.Initial,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val input: AnimeUserInput = AnimeUserInput(),
)

@Immutable
data class AnimeUserInput(
    val query: String = "",
    val filters: List<SearchSectionUiModel> = emptyList(),
)

enum class AnimeSearchStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
