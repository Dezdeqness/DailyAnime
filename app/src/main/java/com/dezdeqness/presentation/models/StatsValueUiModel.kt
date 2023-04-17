package com.dezdeqness.presentation.models

data class StatsValueUiModel(
    val name: String,
    val value: String,
    val currentProgress: Int,
    val maxProgress: Int,
) : AdapterItem()
