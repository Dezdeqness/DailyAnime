package com.dezdeqness.data.anilist.anime.di

import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.data.anilist.anime.datasource.AnilistAnimeRemoteDataSource
import com.dezdeqness.data.anilist.anime.datasource.AnilistAnimeRemoteDataSourceImpl
import com.dezdeqness.data.anilist.anime.repository.AnilistAnimeRepository
import dagger.Binds
import dagger.Module

@Module
abstract class AnilistAnimeDataModule {

    @Binds
    internal abstract fun bindRemoteDataSource(impl: AnilistAnimeRemoteDataSourceImpl): AnilistAnimeRemoteDataSource

    @Binds
    internal abstract fun bindAnimeRepository(impl: AnilistAnimeRepository): AnimeRepository
}
