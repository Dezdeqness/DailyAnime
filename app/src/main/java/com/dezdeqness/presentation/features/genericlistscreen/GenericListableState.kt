package com.dezdeqness.presentation.features.genericlistscreen

import com.dezdeqness.presentation.models.AdapterItem

data class GenericListableState(
    val list: List<AdapterItem> = listOf(),
    val status: GenericListableStatus = GenericListableStatus.Initial,
)

enum class GenericListableStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
