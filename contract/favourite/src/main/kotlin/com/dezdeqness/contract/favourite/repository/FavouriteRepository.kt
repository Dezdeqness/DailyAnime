package com.dezdeqness.contract.favourite.repository

import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType

interface FavouriteRepository {
    suspend fun getFavourites(userId: Long): List<FavouriteEntity>
    suspend fun addToFavourites(targetId: Long, type: FavouriteLinkedType, kind: FavouriteKind? = null): Boolean
    suspend fun removeFromFavourites(targetId: Long, type: FavouriteLinkedType): Boolean
    suspend fun reorderFavourite(targetId: Long, index: Int): Boolean
}
