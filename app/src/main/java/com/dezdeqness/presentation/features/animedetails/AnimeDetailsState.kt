package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.presentation.models.AdapterItem
import com.google.common.collect.ImmutableList

data class AnimeDetailsState(
    val title: String = "",
    val uiModels: ImmutableList<AdapterItem> = ImmutableList.of(),
    val isEditRateFabShown: Boolean = false,
    val status: DetailsStatus = DetailsStatus.Initial,
)

enum class DetailsStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
