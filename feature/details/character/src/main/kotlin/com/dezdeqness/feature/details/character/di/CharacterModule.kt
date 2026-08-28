package com.dezdeqness.feature.details.character.di

import com.dezdeqness.contract.character.repository.CharacterRepository
import com.dezdeqness.feature.details.character.data.datasource.CharacterApiService
import com.dezdeqness.feature.details.character.data.datasource.CharacterRemoteDataSource
import com.dezdeqness.feature.details.character.data.datasource.CharacterRemoteDataSourceImpl
import com.dezdeqness.feature.details.character.data.repository.CharacterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class CharacterModule {

    @Binds
    internal abstract fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    internal abstract fun bindCharacterRemoteDataSource(
        dataSourceImpl: CharacterRemoteDataSourceImpl,
    ): CharacterRemoteDataSource

    companion object {
        @Provides
        internal fun provideCharacterApiService(retrofit: Retrofit): CharacterApiService =
            retrofit.create(CharacterApiService::class.java)
    }
}
