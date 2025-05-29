package com.dezdeqness.presentation.models

import com.dezdeqness.presentation.features.animelist.AnimeUiModel
import com.google.common.collect.ImmutableList


data class AnimeItemListUiModel(
    val list: ImmutableList<AnimeUiModel>
) : AdapterItem()
