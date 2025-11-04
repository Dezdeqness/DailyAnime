package com.dezdeqness.data.datasource

import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.data.FavouriteApiService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.FavouriteMapper
import dagger.Lazy
import javax.inject.Inject

class FavouriteRemoteDataSourceImpl @Inject constructor(
    private val service: Lazy<FavouriteApiService>,
    private val favouriteMapper: FavouriteMapper,
) : FavouriteRemoteDataSource, BaseDataSource() {

    override suspend fun getFavourites(userId: Long): Result<List<FavouriteEntity>> =
        tryWithCatch {
            val response = service.get().getUserFavourites(userId = userId).execute()

            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                val screenshots = favouriteMapper.fromResponse(responseBody)
                Result.success(screenshots)
            } else {
                throw response.createApiException()
            }
        }
}
