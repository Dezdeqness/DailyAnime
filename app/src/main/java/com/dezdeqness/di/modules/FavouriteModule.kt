package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.data.FavouriteApiService
import com.dezdeqness.data.datasource.FavouriteRemoteDataSource
import com.dezdeqness.data.datasource.FavouriteRemoteDataSourceImpl
import com.dezdeqness.data.repository.FavouriteRepositoryImpl
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.feature.favourite.presentation.FavouritesViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
abstract class FavouriteModule {
    companion object {
        @Provides
        fun provideAchievementService(retrofit: Retrofit): FavouriteApiService =
            retrofit.create(FavouriteApiService::class.java)
    }

    @Binds
    abstract fun bindFavouriteRemoteDataSource(
        favouriteRemoteDataSource: FavouriteRemoteDataSourceImpl,
    ): FavouriteRemoteDataSource

    @Binds
    abstract fun bindFavouriteRepository(favouriteRepository: FavouriteRepositoryImpl): FavouriteRepository

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    abstract fun bindFavouritesViewModel(viewModel: FavouritesViewModel): ViewModel

}