package com.dezdeqness.presentation.models

data class RelatedItemUiModel(
    val title: String,
    val anime: AnimeUiModel,
) : AdapterItem()

data class RelatedItemListUiModel(
    val list: List<RelatedItemUiModel>
) : AdapterItem()
