package com.dezdeqness.data.shikimori.anime.di

import com.dezdeqness.contract.anime.repository.AnimeChronologyRepository
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.anime.repository.AnimeSimilarRepository
import com.dezdeqness.data.shikimori.anime.ShikimoriAnimeApiService
import com.dezdeqness.data.shikimori.anime.datasource.ShikimoriAnimeRemoteDataSource
import com.dezdeqness.data.shikimori.anime.datasource.ShikimoriAnimeRemoteDataSourceImpl
import com.dezdeqness.data.shikimori.anime.repository.ShikimoriAnimeChronologyRepositoryImpl
import com.dezdeqness.data.shikimori.anime.repository.ShikimoriAnimeRepositoryImpl
import com.dezdeqness.data.shikimori.anime.repository.ShikimoriAnimeSimilarRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class ShikimoriAnimeDataModule {

    @Binds
    internal abstract fun bindAnimeRemoteDataSource(
        animeRemoteDataSourceImpl: ShikimoriAnimeRemoteDataSourceImpl,
    ): ShikimoriAnimeRemoteDataSource

    @Binds
    internal abstract fun bindAnimeRepository(animeRepositoryImpl: ShikimoriAnimeRepositoryImpl): AnimeRepository

    @Binds
    internal abstract fun bindAnimeChronologyRepository(
        impl: ShikimoriAnimeChronologyRepositoryImpl,
    ): AnimeChronologyRepository

    @Binds
    internal abstract fun bindAnimeSimilarRepository(
        impl: ShikimoriAnimeSimilarRepositoryImpl,
    ): AnimeSimilarRepository

    companion object {
        @Provides
        fun provideAnimeApiService(retrofit: Retrofit): ShikimoriAnimeApiService =
            retrofit.create(ShikimoriAnimeApiService::class.java)
    }
}
