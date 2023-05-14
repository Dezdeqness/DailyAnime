package com.dezdeqness.presentation.models

data class ChronologyUiModel(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val briefInfo: String,
) : AdapterItem()
