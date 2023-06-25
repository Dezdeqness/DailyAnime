package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.models.AnimeUiModel

data class AnimeState(
    val list: List<AnimeUiModel> = listOf(),
    val events: List<Event> = listOf(),
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val hasNextPage: Boolean = false,
    val isErrorStateShowing: Boolean = false,
)
