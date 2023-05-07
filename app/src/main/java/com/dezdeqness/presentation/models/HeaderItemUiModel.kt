package com.dezdeqness.presentation.models

data class HeaderItemUiModel(
    val title: String,
    val imageUrl: String ,
    val ratingScore: Float,
) : AdapterItem()
