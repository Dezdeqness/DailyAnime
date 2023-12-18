package com.dezdeqness.presentation.features.animesimilar

import com.dezdeqness.presentation.models.AdapterItem

data class AnimeSimilarState(
    val list: List<AdapterItem> = listOf(),
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val isErrorStateShowing: Boolean = false,
)
