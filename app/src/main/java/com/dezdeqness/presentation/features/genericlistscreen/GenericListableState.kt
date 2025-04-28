package com.dezdeqness.presentation.features.genericlistscreen

import com.dezdeqness.presentation.models.AdapterItem
import com.google.common.collect.ImmutableList

data class GenericListableState(
    val list: ImmutableList<AdapterItem> = ImmutableList.of(),
    val status: GenericListableStatus = GenericListableStatus.Initial,
)

enum class GenericListableStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
