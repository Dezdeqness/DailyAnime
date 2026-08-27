package com.dezdeqness.data.anime.di

import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.data.anime.AnimeApiService
import com.dezdeqness.data.anime.datasource.AnimeRemoteDataSource
import com.dezdeqness.data.anime.datasource.AnimeRemoteDataSourceImpl
import com.dezdeqness.data.anime.repository.AnimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class AnimeDataModule {

    @Binds
    internal abstract fun bindAnimeRemoteDataSource(
        animeRemoteDataSourceImpl: AnimeRemoteDataSourceImpl,
    ): AnimeRemoteDataSource

    @Binds
    internal abstract fun bindAnimeRepository(animeRepositoryImpl: AnimeRepositoryImpl): AnimeRepository

    companion object {
        @Provides
        fun provideAnimeApiService(retrofit: Retrofit): AnimeApiService =
            retrofit.create(AnimeApiService::class.java)
    }
}
