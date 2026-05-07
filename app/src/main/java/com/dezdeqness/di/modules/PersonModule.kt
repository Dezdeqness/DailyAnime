package com.dezdeqness.di.modules

import com.dezdeqness.data.datasource.PersonRemoteDataSource
import com.dezdeqness.data.datasource.PersonRemoteDataSourceImpl
import com.dezdeqness.data.repository.PersonRepositoryImpl
import com.dezdeqness.domain.repository.PersonRepository
import dagger.Binds
import dagger.Module

@Module
abstract class PersonModule {
    @Binds
    abstract fun bindPersonRepository(repository: PersonRepositoryImpl): PersonRepository

    @Binds
    abstract fun bindPersonRemoteDataSource(
        dataSourceImpl: PersonRemoteDataSourceImpl
    ): PersonRemoteDataSource
}
