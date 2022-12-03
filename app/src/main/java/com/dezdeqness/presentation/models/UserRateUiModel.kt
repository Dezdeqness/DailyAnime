package com.dezdeqness.presentation.models

data class UserRateUiModel(
    val rateId: Long,
    val id: Long,
    val name: String,
    val briefInfo: String,
    val score: String,
    val progress: String,
    val logoUrl: String,
) : AdapterItem()
