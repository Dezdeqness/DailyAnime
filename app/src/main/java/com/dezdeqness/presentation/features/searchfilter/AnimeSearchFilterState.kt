package com.dezdeqness.presentation.features.searchfilter

import androidx.compose.runtime.Immutable
import com.dezdeqness.presentation.models.SearchSectionUiModel
import kotlinx.coroutines.flow.StateFlow

@Immutable
data class AnimeSearchFilterState(
    val items: List<StateFlow<SearchSectionUiModel>> = listOf(),
    val isFilterVisible: Boolean = false,
)
