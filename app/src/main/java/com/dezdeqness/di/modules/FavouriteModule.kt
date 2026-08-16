package com.dezdeqness.di.modules

import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.contract.favourite.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.data.FavouriteApiService
import com.dezdeqness.data.datasource.FavouriteRemoteDataSource
import com.dezdeqness.data.datasource.FavouriteRemoteDataSourceImpl
import com.dezdeqness.data.repository.FavouriteRepositoryImpl
import com.dezdeqness.domain.favourite.usecases.ObserveFavouriteStatusUseCaseImpl
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
    }

    @Binds
    abstract fun bindObserveFavouriteStatusUseCase(
        observeFavouriteStatusUseCase: ObserveFavouriteStatusUseCaseImpl,
    ): ObserveFavouriteStatusUseCase

    @Binds
    abstract fun bindFavouriteRemoteDataSource(
        favouriteRemoteDataSource: FavouriteRemoteDataSourceImpl,
    ): FavouriteRemoteDataSource

    @Binds
    abstract fun bindFavouriteRepository(favouriteRepository: FavouriteRepositoryImpl): FavouriteRepository
}
