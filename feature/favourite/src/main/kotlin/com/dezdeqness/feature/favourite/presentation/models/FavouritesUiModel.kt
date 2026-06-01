package com.dezdeqness.feature.favourite.presentation.models

import com.dezdeqness.contract.favourite.model.FavouriteType

data class FavouritesUiModel(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val type: FavouriteType,
)
