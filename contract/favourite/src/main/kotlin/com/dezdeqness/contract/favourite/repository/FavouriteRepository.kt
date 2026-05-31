package com.dezdeqness.contract.favourite.repository

import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.contract.favourite.model.FavouritesCacheState
import kotlinx.coroutines.flow.StateFlow

interface FavouriteRepository {

    val favourites: StateFlow<FavouritesCacheState>

    suspend fun fetchFavourites(userId: Long, force: Boolean = false): Result<Unit>

    suspend fun toggleFavourite(
        userId: Long,
        targetId: Long,
        type: FavouriteLinkedType,
        kind: FavouriteKind? = null,
    ): Result<Unit>

    fun clearCache()
}
