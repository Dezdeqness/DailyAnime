package com.dezdeqness.presentation.models

data class SimilarUiModel(
    val id: Long,
    val name: String,
    val briefInfo: String,
    val score: String,
    val logoUrl: String,
) : AdapterItem()