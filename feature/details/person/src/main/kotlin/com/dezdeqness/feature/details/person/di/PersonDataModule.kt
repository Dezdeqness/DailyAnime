package com.dezdeqness.feature.details.person.di

import com.dezdeqness.contract.person.repository.PersonRepository
import com.dezdeqness.feature.details.person.data.PersonRemoteDataSource
import com.dezdeqness.feature.details.person.data.PersonRemoteDataSourceImpl
import com.dezdeqness.feature.details.person.data.PersonRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PersonDataModule {

    @Binds
    internal abstract fun bindPersonRepository(repository: PersonRepositoryImpl): PersonRepository

    @Binds
    internal abstract fun bindPersonRemoteDataSource(dataSourceImpl: PersonRemoteDataSourceImpl): PersonRemoteDataSource
}
