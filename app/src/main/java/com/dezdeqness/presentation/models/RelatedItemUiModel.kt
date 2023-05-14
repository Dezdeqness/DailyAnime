package com.dezdeqness.presentation.models

data class RelatedItemUiModel(
    val id: Long,
    val type: String,
    val briefInfo: String,
    val logoUrl: String,
) : AdapterItem()

data class RelatedItemListUiModel(
    val list: List<RelatedItemUiModel>
) : AdapterItem()
