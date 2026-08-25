package com.dezdeqness.feature.favourite.di

import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.feature.favourite.data.FavouriteApiService
import com.dezdeqness.feature.favourite.data.FavouriteRemoteDataSource
import com.dezdeqness.feature.favourite.data.FavouriteRemoteDataSourceImpl
import com.dezdeqness.feature.favourite.data.FavouriteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class FavouriteDataModule {

    companion object {
        @Provides
        internal fun provideFavouriteApiService(retrofit: Retrofit): FavouriteApiService =
            retrofit.create(FavouriteApiService::class.java)
    }

    @Binds
    internal abstract fun bindFavouriteRemoteDataSource(
        favouriteRemoteDataSource: FavouriteRemoteDataSourceImpl,
    ): FavouriteRemoteDataSource

    @Binds
    internal abstract fun bindFavouriteRepository(favouriteRepository: FavouriteRepositoryImpl): FavouriteRepository
}
