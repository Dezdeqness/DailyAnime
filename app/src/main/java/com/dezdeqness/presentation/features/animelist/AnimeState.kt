package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.models.AnimeUiItem

data class AnimeState(
    val list: List<AnimeUiItem> = listOf(),
    val isListChanged: Boolean = false,
    val events: List<Event> = listOf(),
)
