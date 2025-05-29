package com.dezdeqness.di.modules

import com.dezdeqness.data.CharacterApiService
import com.dezdeqness.data.datasource.CharacterRemoteDataSource
import com.dezdeqness.data.datasource.CharacterRemoteDataSourceImpl
import com.dezdeqness.data.repository.CharacterRepositoryImpl
import com.dezdeqness.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class CharacterModule {
    @Binds
    abstract fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    abstract fun bindCharacterRemoteDataSource(
        dataSourceImpl: CharacterRemoteDataSourceImpl
    ): CharacterRemoteDataSource

    companion object {
        @Provides
        fun provideCharacterApiService(retrofit: Retrofit): CharacterApiService =
            retrofit.create(CharacterApiService::class.java)
    }
}
