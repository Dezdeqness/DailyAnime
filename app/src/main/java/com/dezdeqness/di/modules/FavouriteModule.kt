package com.dezdeqness.di.modules

import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.data.FavouriteApiService
import com.dezdeqness.data.datasource.FavouriteRemoteDataSource
import com.dezdeqness.data.datasource.FavouriteRemoteDataSourceImpl
import com.dezdeqness.data.repository.FavouriteRepositoryImpl
import com.dezdeqness.domain.usecases.FetchFavouritesUseCase
import com.dezdeqness.domain.usecases.ObserveFavouriteStatusUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class FavouriteModule {
    companion object {
        @Provides
        fun provideFavouriteApiService(retrofit: Retrofit): FavouriteApiService =
            retrofit.create(FavouriteApiService::class.java)

        @Provides
        fun provideObserveFavouriteStatusUseCase(favouriteRepository: FavouriteRepository) =
            ObserveFavouriteStatusUseCase(favouriteRepository = favouriteRepository)

        @Provides
        fun provideFetchFavouritesUseCase(favouriteRepository: FavouriteRepository) =
            FetchFavouritesUseCase(favouriteRepository = favouriteRepository)
    }

    @Binds
    abstract fun bindFavouriteRemoteDataSource(
        favouriteRemoteDataSource: FavouriteRemoteDataSourceImpl,
    ): FavouriteRemoteDataSource

    @Binds
    abstract fun bindFavouriteRepository(favouriteRepository: FavouriteRepositoryImpl): FavouriteRepository
}
