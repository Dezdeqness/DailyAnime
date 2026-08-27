package com.dezdeqness.feature.userrate.di

import android.content.Context
import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import com.dezdeqness.feature.userrate.data.UserRateDatabase
import com.dezdeqness.feature.userrate.data.UserRatesApiService
import com.dezdeqness.feature.userrate.data.UserRatesDao
import com.dezdeqness.feature.userrate.data.UserRatesLocalDataSource
import com.dezdeqness.feature.userrate.data.UserRatesLocalDataSourceImpl
import com.dezdeqness.feature.userrate.data.UserRatesRemoteDataSource
import com.dezdeqness.feature.userrate.data.UserRatesRemoteDataSourceImpl
import com.dezdeqness.feature.userrate.data.UserRatesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
abstract class UserRatesDataModule {

    @Binds
    internal abstract fun bindUserRatesRepository(
        userRatesRepositoryImpl: UserRatesRepositoryImpl,
    ): UserRatesRepository

    @Binds
    internal abstract fun bindUserRatesRemoteDataSource(
        userRatesRemoteDataSource: UserRatesRemoteDataSourceImpl,
    ): UserRatesRemoteDataSource

    @Binds
    internal abstract fun bindUserRatesLocalDataSource(
        userRatesLocalDataSource: UserRatesLocalDataSourceImpl,
    ): UserRatesLocalDataSource

    companion object {
        @Provides
        internal fun provideUserRatesApiService(retrofit: Retrofit): UserRatesApiService =
            retrofit.create(UserRatesApiService::class.java)

        @Singleton
        @Provides
        internal fun provideUserRateDatabase(context: Context): UserRateDatabase =
            UserRateDatabase.build(context)

        @Provides
        internal fun provideUserRatesDao(database: UserRateDatabase): UserRatesDao =
            database.userRatesDao()
    }
}
