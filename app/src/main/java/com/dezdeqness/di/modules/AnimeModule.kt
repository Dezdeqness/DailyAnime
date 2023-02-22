package com.dezdeqness.di.modules

import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.data.datasource.AnimeRemoteDataSourceImpl
import com.dezdeqness.data.repository.AnimeRepositoryImpl
import com.dezdeqness.domain.repository.AnimeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [PersonalModule::class])
abstract class AnimeModule {

    companion object {
        @Provides
        fun provideAnimeApiService(retrofit: Retrofit): AnimeApiService =
            retrofit.create(AnimeApiService::class.java)
    }

    @Binds
    abstract fun bindAnimeRemoteDataSource(animeRemoteDataSourceImpl: AnimeRemoteDataSourceImpl): AnimeRemoteDataSource

    @Binds
    abstract fun bindAnimeRepository(animeRepositoryImpl: AnimeRepositoryImpl): AnimeRepository

}
