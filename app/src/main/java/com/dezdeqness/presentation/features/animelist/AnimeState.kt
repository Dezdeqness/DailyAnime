package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.presentation.models.AnimeUiModel

data class AnimeState(
    val list: List<AnimeUiModel> = listOf(),
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val hasNextPage: Boolean = false,
    val isErrorStateShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
)
