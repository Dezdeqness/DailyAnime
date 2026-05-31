package com.dezdeqness.contract.favourite.model

sealed interface FavouritesCacheState {
    data object Empty : FavouritesCacheState
    data object Loading : FavouritesCacheState
    data class Loaded(
        val items: List<FavouriteEntity>,
        val loadedAtMillis: Long,
    ) : FavouritesCacheState

    data class Error(val throwable: Throwable) : FavouritesCacheState
}
