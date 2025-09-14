package com.dezdeqness.di.modules

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.history.repository.HistoryRepository
import com.dezdeqness.data.core.CookieCleaner
import com.dezdeqness.data.database.ShikimoriDatabase
import com.dezdeqness.data.datasource.AccountRemoteDataSource
import com.dezdeqness.data.datasource.AccountRemoteDataSourceImpl
import com.dezdeqness.data.datasource.db.AccountLocalDataSource
import com.dezdeqness.data.datasource.db.AccountLocalDataSourceImpl
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.data.repository.UserRepositoryImpl
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.domain.usecases.GetUserUseCase
import com.dezdeqness.domain.usecases.LoginUseCase
import com.dezdeqness.domain.usecases.LogoutUseCase
import com.dezdeqness.domain.usecases.RefreshTokenUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AccountModule {

    companion object {

        @Provides
        fun provideLoginUseCase(
            userRepository: UserRepository,
            authRepository: AuthRepository,
        ) = LoginUseCase(
            userRepository = userRepository,
            authRepository = authRepository,
        )

        @Provides
        fun provideLogoutUseCase(
            userRepository: UserRepository,
            authRepository: AuthRepository,
        ) = LogoutUseCase(
            userRepository = userRepository,
            authRepository = authRepository,
        )

        @Singleton
        @Provides
        fun bindAccountRepository(
            accountRemoteDataSource: AccountRemoteDataSource,
            accountLocalDataSource: AccountLocalDataSource,
            tokenManager: TokenManager,
            cookieCleaner: CookieCleaner,
        ): UserRepositoryImpl =
            UserRepositoryImpl(
                accountRemoteDataSource = accountRemoteDataSource,
                accountLocalDataSource = accountLocalDataSource,
                tokenManager = tokenManager,
                cookieCleaner = cookieCleaner,
            )

        @Singleton
        @Provides
        fun providerAccountRepository(
            repository: UserRepositoryImpl,
        ): UserRepository = repository

        @Singleton
        @Provides
        fun providerHistoryRepository(
            repository: UserRepositoryImpl,
        ): HistoryRepository = repository

        @Singleton
        @Provides
        fun providerAuthRepository(
            repository: UserRepositoryImpl,
        ): AuthRepository = repository

        @Provides
        fun provideRefreshTokenUseCase(authRepository: AuthRepository) = RefreshTokenUseCase(
            authRepository = authRepository
        )

        @Provides
        fun provideGetProfileUseCase(userRepository: UserRepository) = GetUserUseCase(
            userRepository = userRepository
        )

        @Provides
        fun provideAccountDao(shikimoriDatabase: ShikimoriDatabase) =
            shikimoriDatabase.accountDao()

    }

    @Binds
    abstract fun bindAccountRemoteDataSource(dataSourceImpl: AccountRemoteDataSourceImpl): AccountRemoteDataSource

    @Binds
    abstract fun bindAccountLocalDataSource(dataSourceImpl: AccountLocalDataSourceImpl): AccountLocalDataSource

}
