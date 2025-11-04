package com.dezdeqness.contract.favourite.repository

import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType

interface FavouriteRepository {
    suspend fun getFavourites(userId: Long): Result<List<FavouriteEntity>>
    suspend fun addToFavourites(
        targetId: Long,
        type: FavouriteLinkedType,
        kind: FavouriteKind? = null
    ): Result<Unit>

    suspend fun removeFromFavourites(targetId: Long, type: FavouriteLinkedType): Result<Unit>
    suspend fun reorderFavourite(targetId: Long, index: Int): Result<Unit>
}
