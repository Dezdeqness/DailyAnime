package com.dezdeqness.data.repository

import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.data.datasource.FavouriteRemoteDataSource
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val remoteDataSource: FavouriteRemoteDataSource,
) : FavouriteRepository {
    override suspend fun getFavourites(userId: Long) =
        remoteDataSource.getFavourites(userId = userId)

    override suspend fun addToFavourites(
        targetId: Long,
        type: FavouriteLinkedType,
        kind: FavouriteKind?
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavourites(
        targetId: Long,
        type: FavouriteLinkedType
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun reorderFavourite(
        targetId: Long,
        index: Int
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}
