package com.dezdeqness.di.modules

import com.dezdeqness.data.datasource.HomeRemoteDatasource
import com.dezdeqness.data.datasource.HomeRemoteDatasourceImpl
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.data.repository.HomeRepositoryImpl
import com.dezdeqness.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class HomeModule {

    companion object {

        @Provides
        fun provideHomeGenresProvider() = HomeGenresProvider()
    }

    @Binds
    abstract fun bindHomeRepository(repository: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun bindHomeRemoteDataSource(dataSourceImpl: HomeRemoteDatasourceImpl): HomeRemoteDatasource
}
