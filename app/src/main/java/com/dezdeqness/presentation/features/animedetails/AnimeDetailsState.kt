package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.presentation.models.AdapterItem

data class AnimeDetailsState(
    val title: String = "",
    val uiModels: List<AdapterItem> = listOf(),
    val isEditRateFabShown: Boolean = false,
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isToolbarVisible: Boolean = false,
    val isErrorStateShowing: Boolean = false,
)
