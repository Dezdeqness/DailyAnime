package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.models.AnimeUiModel

data class AnimeState(
    val list: List<AnimeUiModel> = listOf(),
    val events: List<Event> = listOf(),
    val isRefreshing: Boolean = false,
    val hasNextPage: Boolean = false,
)
