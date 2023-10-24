package com.dezdeqness.di.modules

import com.dezdeqness.data.UserRatesApiService
import com.dezdeqness.data.database.ShikimoriDatabase
import com.dezdeqness.data.datasource.UserRatesRemoteDataSource
import com.dezdeqness.data.datasource.UserRatesRemoteDataSourceImpl
import com.dezdeqness.data.datasource.db.UserRatesLocalDataSource
import com.dezdeqness.data.datasource.db.UserRatesLocalDataSourceImpl
import com.dezdeqness.data.repository.PersonalListFilterRepositoryImpl
import com.dezdeqness.data.repository.UserRatesRepositoryImpl
import com.dezdeqness.domain.repository.PersonalListFilterRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class PersonalModule {

    companion object {

        @Provides
        fun provideAccountApiService(retrofit: Retrofit): UserRatesApiService =
            retrofit.create(UserRatesApiService::class.java)

        @Provides
        fun provideUserRatesDao(shikimoriDatabase: ShikimoriDatabase) =
            shikimoriDatabase.userRatesDao()
    }

    @Binds
    abstract fun bindUserRatesRemoteDataSource(
        userRatesRemoteDataSource: UserRatesRemoteDataSourceImpl,
        ): UserRatesRemoteDataSource

    @Binds
    abstract fun bindUserRatesLocalDataSource(
        userRatesLocalDataSource: UserRatesLocalDataSourceImpl,
        ): UserRatesLocalDataSource

    @Binds
    abstract fun bindUserRatesRepository(
        userRatesRepositoryImpl: UserRatesRepositoryImpl,
        ): UserRatesRepository

    @Binds
    abstract fun bindPersonalListFilterRepository(
        userRatesRepositoryImpl: PersonalListFilterRepositoryImpl,
        ): PersonalListFilterRepository

}
