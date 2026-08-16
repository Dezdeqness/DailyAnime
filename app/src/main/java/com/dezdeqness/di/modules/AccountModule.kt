package com.dezdeqness.di.modules

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.auth.usecases.LoginUseCase
import com.dezdeqness.contract.auth.usecases.LogoutUseCase
import com.dezdeqness.contract.auth.usecases.RefreshTokenUseCase
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.contract.history.repository.HistoryRepository
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.data.core.CookieCleaner
import com.dezdeqness.data.database.ShikimoriDatabase
import com.dezdeqness.data.datasource.AccountRemoteDataSource
import com.dezdeqness.data.datasource.AccountRemoteDataSourceImpl
import com.dezdeqness.data.datasource.db.AccountLocalDataSource
import com.dezdeqness.data.datasource.db.AccountLocalDataSourceImpl
import com.dezdeqness.data.datasource.db.dao.AccountSessionDao
import com.dezdeqness.data.manager.SessionManagerImpl
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.data.repository.UserRepositoryImpl
import com.dezdeqness.domain.auth.usecases.LoginUseCaseImpl
import com.dezdeqness.domain.auth.usecases.LogoutUseCaseImpl
import com.dezdeqness.domain.auth.usecases.RefreshTokenUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AccountModule {

    companion object {

        @Singleton
        @Provides
        fun bindAccountRepository(
            accountRemoteDataSource: AccountRemoteDataSource,
            accountLocalDataSource: AccountLocalDataSource,
            tokenManager: TokenManager,
            cookieCleaner: CookieCleaner,
        ): UserRepositoryImpl = UserRepositoryImpl(
            accountRemoteDataSource = accountRemoteDataSource,
            accountLocalDataSource = accountLocalDataSource,
            tokenManager = tokenManager,
            cookieCleaner = cookieCleaner,
        )

        @Singleton
        @Provides
        fun providerAccountRepository(repository: UserRepositoryImpl): UserRepository = repository

        @Singleton
        @Provides
        fun providerHistoryRepository(repository: UserRepositoryImpl): HistoryRepository = repository

        @Singleton
        @Provides
        fun providerAuthRepository(repository: UserRepositoryImpl): AuthRepository = repository

        @Provides
        fun provideAccountDao(shikimoriDatabase: ShikimoriDatabase) = shikimoriDatabase.accountDao()

        @Provides
        fun provideAccountSessionDao(shikimoriDatabase: ShikimoriDatabase) = shikimoriDatabase.accountSessionDao()

        @Singleton
        @Provides
        fun provideSessionManager(
            loginUseCase: LoginUseCase,
            logoutUseCase: LogoutUseCase,
            refreshTokenUseCase: RefreshTokenUseCase,
            authRepository: AuthRepository,
            userRepository: UserRepository,
            accountSessionDao: AccountSessionDao,
            favouriteRepository: FavouriteRepository,
        ): SessionManager = SessionManagerImpl(
            loginUseCase = loginUseCase,
            logoutUseCase = logoutUseCase,
            refreshTokenUseCase = refreshTokenUseCase,
            authRepository = authRepository,
            userRepository = userRepository,
            accountSessionDao = accountSessionDao,
            favouriteRepository = favouriteRepository,
        )
    }

    @Binds
    abstract fun bindAccountRemoteDataSource(dataSourceImpl: AccountRemoteDataSourceImpl): AccountRemoteDataSource

    @Binds
    abstract fun bindAccountLocalDataSource(dataSourceImpl: AccountLocalDataSourceImpl): AccountLocalDataSource

    @Binds
    abstract fun bindLoginUseCase(loginUseCase: LoginUseCaseImpl): LoginUseCase

    @Binds
    abstract fun bindLogoutUseCase(logoutUseCase: LogoutUseCaseImpl): LogoutUseCase

    @Binds
    abstract fun bindRefreshTokenUseCase(refreshTokenUseCase: RefreshTokenUseCaseImpl): RefreshTokenUseCase
}
