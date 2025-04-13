package com.dezdeqness.presentation.models

import com.google.common.collect.ImmutableList

data class RelatedItemUiModel(
    val id: Long,
    val type: String,
    val briefInfo: String,
    val logoUrl: String,
) : AdapterItem()

data class RelatedItemListUiModel(
    val list: ImmutableList<RelatedItemUiModel>
) : AdapterItem()
