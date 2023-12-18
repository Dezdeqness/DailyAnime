package com.dezdeqness.presentation.features.animechronology

import com.dezdeqness.presentation.models.AdapterItem

data class AnimeChronologyState(
    val list: List<AdapterItem> = listOf(),
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val isErrorStateShowing: Boolean = false,
)
