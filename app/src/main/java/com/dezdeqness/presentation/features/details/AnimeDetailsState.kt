package com.dezdeqness.presentation.features.details

import com.dezdeqness.presentation.models.AdapterItem
import com.google.common.collect.ImmutableList

data class AnimeDetailsState(
    val title: String = "",
    val uiModels: ImmutableList<AdapterItem> = ImmutableList.of(),
    val isEditRateFabShown: Boolean = false,
    val status: DetailsStatus = DetailsStatus.Initial,
    val currentBottomSheet: BottomSheet = BottomSheet.None,
)

enum class DetailsStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}

sealed interface BottomSheet {
    data object None : BottomSheet
    data class EditRate(
        val userRateId: Long,
        val title: String,
    ) : BottomSheet
}