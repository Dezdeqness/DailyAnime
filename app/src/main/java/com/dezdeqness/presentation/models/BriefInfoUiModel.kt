package com.dezdeqness.presentation.models

import com.google.common.collect.ImmutableList

data class BriefInfoUiModel(
    val info: String,
    val title: String,
) : AdapterItem()

data class BriefInfoUiModelList(
    val list: ImmutableList<BriefInfoUiModel>
) : AdapterItem()
