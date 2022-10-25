package com.dezdeqness.presentation.models

data class BriefInfoUiModel(
    val info: String,
    val title: String,
) : AdapterItem()

data class BriefInfoUiModelList(
    val list: List<BriefInfoUiModel>
) : AdapterItem()
