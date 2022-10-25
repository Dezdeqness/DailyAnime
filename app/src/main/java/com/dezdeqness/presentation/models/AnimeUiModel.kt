package com.dezdeqness.presentation.models

data class AnimeUiModel(
    val id: Long,
    val briefInfo: String,
    val kind: String,
    val logoUrl: String,
    val airedYear: String,
) : AdapterItem()
