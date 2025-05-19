package com.dezdeqness.presentation.features.animelist

import androidx.compose.runtime.Immutable

@Immutable
data class AnimeSearchState(
    val list: List<AnimeUiModel> = listOf(),
    val status: AnimeSearchStatus = AnimeSearchStatus.Initial,
    private val isInitialLoadingIndicatorShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val hasNextPage: Boolean = false,
    val isScrollNeed: Boolean = false,
)

enum class AnimeSearchStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
