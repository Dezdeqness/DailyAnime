package com.dezdeqness.presentation.features.animesimilar

import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.models.AdapterItem

data class AnimeSimilarState(
    val list: List<AdapterItem> = listOf(),
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val events: List<Event> = listOf(),
)
