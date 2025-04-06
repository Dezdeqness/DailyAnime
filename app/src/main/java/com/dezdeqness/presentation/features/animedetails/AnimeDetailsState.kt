package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.presentation.models.AdapterItem
import com.google.common.collect.ImmutableList

data class AnimeDetailsState(
    val title: String = "",
    val uiModels: ImmutableList<AdapterItem> = ImmutableList.of(),
    val isEditRateFabShown: Boolean = false,
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isToolbarVisible: Boolean = false,
    val isErrorStateShowing: Boolean = false,
)
