package com.dezdeqness.di.source.unsupported

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.contract.favourite.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import com.dezdeqness.domain.favourite.usecases.ObserveFavouriteStatusUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UnsupportedAccountModule {

    @Binds
    abstract fun bindAuthRepository(impl: UnsupportedAuthRepository): AuthRepository

    @Binds
    abstract fun bindSessionManager(impl: UnsupportedSessionManager): SessionManager

    @Binds
    abstract fun bindUserRatesRepository(impl: UnsupportedUserRatesRepository): UserRatesRepository

    @Binds
    abstract fun bindFavouriteRepository(impl: UnsupportedFavouriteRepository): FavouriteRepository

    @Binds
    abstract fun bindObserveFavouriteStatusUseCase(
        impl: ObserveFavouriteStatusUseCaseImpl,
    ): ObserveFavouriteStatusUseCase
}
