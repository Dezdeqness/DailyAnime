package com.dezdeqness.feature.search.presentation

import androidx.compose.runtime.Immutable
import com.dezdeqness.architecture.store.paging.PagedContent
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel

@Immutable
data class AnimeSearchState(
    val content: PagedContent<AnimeUiModel> = PagedContent.Loading,
    val isRefreshing: Boolean = false,
    val filterButton: FilterButtonState = FilterButtonState.Shown(isApplied = false),
)

sealed interface FilterButtonState {
    data object Hidden : FilterButtonState
    data class Shown(val isApplied: Boolean) : FilterButtonState
}
