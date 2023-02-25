package com.dezdeqness.presentation.features.personallist.list

import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.RibbonStatusUiModel

data class PersonalListState(
    val ribbon: List<RibbonStatusUiModel> = listOf(),
    val items: List<AdapterItem> = listOf(),
    val events: List<Event> = listOf(),
    val isPullDownRefreshing: Boolean = false,
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
)
