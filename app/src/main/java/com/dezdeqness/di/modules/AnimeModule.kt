package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.data.datasource.AnimeRemoteDataSourceImpl
import com.dezdeqness.data.repository.AnimeRepositoryImpl
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.domain.usecases.GetAnimeListUseCase
import com.dezdeqness.presentation.features.animelist.AnimeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(includes = [DataModule::class])
abstract class AnimeModule {

    companion object {
        @Provides
        fun provideMangaApiService(retrofit: Retrofit): AnimeApiService =
            retrofit.create(AnimeApiService::class.java)

        @Provides
        fun provideUseCase(animeRepository: AnimeRepository) = GetAnimeListUseCase(
            animeRepository = animeRepository
        )
    }

    @Binds
    abstract fun bindAnimeRemoteDataSource(animeRemoteDataSourceImpl: AnimeRemoteDataSourceImpl): AnimeRemoteDataSource

    @Binds
    abstract fun bindAnimeRepository(animeRepositoryImpl: AnimeRepositoryImpl): AnimeRepository

    @Binds
    @IntoMap
    @ViewModelKey(AnimeViewModel::class)
    abstract fun bindViewModel(viewModel: AnimeViewModel): ViewModel
}
