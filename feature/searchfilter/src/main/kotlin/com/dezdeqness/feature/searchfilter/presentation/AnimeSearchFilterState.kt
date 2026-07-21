package com.dezdeqness.feature.searchfilter.presentation

import androidx.compose.runtime.Stable
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.searchfilter.presentation.models.SelectedCell
import kotlinx.coroutines.flow.MutableStateFlow

@Stable
data class AnimeSearchFilterState(
    val items: List<MutableStateFlow<SearchSectionUiModel>> = listOf(),
    val selectedCells: Set<SelectedCell> = setOf(),
    val generalSelectedCells: Boolean = false,
    val isFilterVisible: Boolean = false,
)
