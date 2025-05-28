package com.dezdeqness.di.modules

import com.dezdeqness.data.datasource.CharacterRemoteDataSource
import com.dezdeqness.data.datasource.CharacterRemoteDataSourceImpl
import com.dezdeqness.data.repository.CharacterRepositoryImpl
import com.dezdeqness.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module

@Module
abstract class CharacterModule {
    @Binds
    abstract fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    abstract fun bindCharacterRemoteDataSource(
        dataSourceImpl: CharacterRemoteDataSourceImpl
    ): CharacterRemoteDataSource
}
