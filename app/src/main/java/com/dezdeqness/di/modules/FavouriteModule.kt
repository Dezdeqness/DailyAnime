package com.dezdeqness.di.modules

import com.dezdeqness.contract.favourite.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.domain.favourite.usecases.ObserveFavouriteStatusUseCaseImpl
import com.dezdeqness.feature.favourite.di.FavouriteModule as FeatureFavouriteModule
import dagger.Binds
import dagger.Module

@Module(includes = [FeatureFavouriteModule::class])
abstract class FavouriteModule {

    @Binds
    abstract fun bindObserveFavouriteStatusUseCase(
        observeFavouriteStatusUseCase: ObserveFavouriteStatusUseCaseImpl,
    ): ObserveFavouriteStatusUseCase
}
