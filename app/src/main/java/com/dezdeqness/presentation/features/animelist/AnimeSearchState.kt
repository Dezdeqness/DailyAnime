package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.presentation.models.AnimeUiModel

data class AnimeSearchState(
    val list: List<AnimeUiModel> = listOf(),
    val isEmptyStateShowing: Boolean = false,
    private val isInitialLoadingIndicatorShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val hasNextPage: Boolean = false,
    val isErrorStateShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
) {

    val isLoadingStateShowing get() =
        list.isEmpty() && isEmptyStateShowing.not() && isInitialLoadingIndicatorShowing

//    val isPageLoading get() =
//        isLoadingStateShowing || isPullDownRefreshing || isLoadMoreLoading
}
