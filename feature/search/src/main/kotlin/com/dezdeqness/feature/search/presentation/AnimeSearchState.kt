package com.dezdeqness.feature.search.presentation

import androidx.compose.runtime.Immutable
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel

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
