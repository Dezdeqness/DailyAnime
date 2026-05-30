package com.dezdeqness.contract.favourite.repository

import com.dezdeqness.contract.favourite.model.ActionFavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType

interface FavouriteRepository {
    suspend fun getFavourites(userId: Long): Result<List<FavouriteEntity>>
    suspend fun addToFavourites(
        targetId: Long,
        type: FavouriteLinkedType,
        kind: FavouriteKind? = null
    ): Result<ActionFavouriteEntity>

    suspend fun removeFromFavourites(targetId: Long, type: FavouriteLinkedType): Result<ActionFavouriteEntity>
}
