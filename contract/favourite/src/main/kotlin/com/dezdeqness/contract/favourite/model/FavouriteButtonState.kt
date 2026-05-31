package com.dezdeqness.contract.favourite.model

sealed interface FavouriteButtonState {
    data object Hidden : FavouriteButtonState
    data object Disabled : FavouriteButtonState
    data class Idle(val isFavourite: Boolean) : FavouriteButtonState
    data class Processing(val targetIsFavourite: Boolean) : FavouriteButtonState
}
