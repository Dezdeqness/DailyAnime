package com.dezdeqness.feature.favourite.data

import com.dezdeqness.contract.favourite.model.ActionFavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import dagger.Lazy
import javax.inject.Inject

internal class FavouriteRemoteDataSourceImpl @Inject constructor(
    private val service: Lazy<FavouriteApiService>,
    private val favouriteMapper: FavouriteMapper,
) : FavouriteRemoteDataSource, BaseDataSource() {

    override suspend fun getFavourites(userId: Long): Result<List<FavouriteEntity>> = tryWithCatch {
        val response = service.get().getUserFavourites(userId = userId).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val favourites = favouriteMapper.fromResponse(responseBody)
            Result.success(favourites)
        } else {
            throw response.createApiException()
        }
    }

    override suspend fun addToFavourites(
        targetId: Long,
        type: FavouriteLinkedType,
        kind: FavouriteKind?,
    ): Result<ActionFavouriteEntity> = tryWithCatch {
        val response = if (type == FavouriteLinkedType.PERSON) {
            service.get().addFavoriteWithKind(
                linkedType = type.type,
                linkedId = targetId,
                kind = kind?.kind.orEmpty(),
            ).execute()
        } else {
            service.get().addFavorite(linkedType = type.type, linkedId = targetId).execute()
        }

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val actionEntity = favouriteMapper.fromResponse(responseBody)
            Result.success(actionEntity)
        } else {
            throw response.createApiException()
        }
    }

    override suspend fun removeFromFavourites(
        targetId: Long,
        type: FavouriteLinkedType,
    ): Result<ActionFavouriteEntity> = tryWithCatch {
        val response = service.get().deleteFavorite(linkedType = type.type, linkedId = targetId).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val actionEntity = favouriteMapper.fromResponse(responseBody)
            Result.success(actionEntity)
        } else {
            throw response.createApiException()
        }
    }
}
