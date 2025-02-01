package com.dezdeqness.presentation.features.searchfilter

import androidx.compose.runtime.Stable
import com.dezdeqness.presentation.models.SearchSectionUiModel
import kotlinx.coroutines.flow.MutableStateFlow

@Stable
data class AnimeSearchFilterState(
    val items: List<MutableStateFlow<SearchSectionUiModel>> = listOf(),
    val selectedCells: Set<SelectedCell> = setOf(),
    val generalSelectedCells: Boolean = false,
    val isFilterVisible: Boolean = false,
)

data class SelectedCell(
    val sectionId: String,
    val id: String,
    val displayName: String,
)
