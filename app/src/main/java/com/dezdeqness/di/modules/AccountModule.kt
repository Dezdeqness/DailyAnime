package com.dezdeqness.di.modules

import com.dezdeqness.data.core.CookieCleaner
import com.dezdeqness.data.database.ShikimoriDatabase
import com.dezdeqness.data.datasource.AccountRemoteDataSource
import com.dezdeqness.data.datasource.AccountRemoteDataSourceImpl
import com.dezdeqness.data.datasource.db.AccountLocalDataSource
import com.dezdeqness.data.datasource.db.AccountLocalDataSourceImpl
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.data.repository.AccountRepositoryImpl
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.GetProfileUseCase
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
        fun provideLoginUseCase(accountRepository: AccountRepository) = LoginUseCase(
            accountRepository = accountRepository
        )

        @Provides
        fun provideLogoutUseCase(accountRepository: AccountRepository) = LogoutUseCase(
            accountRepository = accountRepository
        )

        @Singleton
        @Provides
        fun bindAccountRepository(
            accountRemoteDataSource: AccountRemoteDataSource,
            accountLocalDataSource: AccountLocalDataSource,
            tokenManager: TokenManager,
            cookieCleaner: CookieCleaner,
        ): AccountRepository =
            AccountRepositoryImpl(
                accountRemoteDataSource = accountRemoteDataSource,
                accountLocalDataSource = accountLocalDataSource,
                tokenManager = tokenManager,
                cookieCleaner = cookieCleaner,
            )

        @Provides
        fun provideRefreshTokenUseCase(accountRepository: AccountRepository) = RefreshTokenUseCase(
            accountRepository = accountRepository
        )

        @Provides
        fun provideGetProfileUseCase(accountRepository: AccountRepository) = GetProfileUseCase(
            accountRepository = accountRepository
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
