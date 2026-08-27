package com.dezdeqness.feature.home.di

import com.dezdeqness.contract.home.repository.HomeRepository
import com.dezdeqness.feature.home.data.datasource.HomeRemoteDatasource
import com.dezdeqness.feature.home.data.datasource.HomeRemoteDatasourceImpl
import com.dezdeqness.feature.home.data.repository.HomeRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class HomeDataModule {

    @Binds
    internal abstract fun bindHomeRepository(repository: HomeRepositoryImpl): HomeRepository

    @Binds
    internal abstract fun bindHomeRemoteDataSource(dataSourceImpl: HomeRemoteDatasourceImpl): HomeRemoteDatasource
}
