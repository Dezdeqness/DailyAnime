package com.dezdeqness.data.datasource

import com.dezdeqness.contract.favourite.model.FavouriteEntity

interface FavouriteRemoteDataSource {
    suspend fun getFavourites(userId: Long): Result<List<FavouriteEntity>>
}
