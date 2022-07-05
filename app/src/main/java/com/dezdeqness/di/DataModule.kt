package com.dezdeqness.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.presentation.features.animelist.AnimeViewModel
import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.data.datasource.AnimeRemoteDataSourceImpl
import com.dezdeqness.data.mapper.ApiErrorMapper
import com.dezdeqness.data.repository.AnimeRepositoryImpl
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.domain.mapper.ErrorMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(includes = [RemoteModule::class])
abstract class DataModule {

    companion object {

        @Provides
        fun provideMangaApiService(retrofit: Retrofit): AnimeApiService =
            retrofit.create(AnimeApiService::class.java)

    }

    @Binds
    abstract fun bindErrorMapper(apiErrorMapper: ApiErrorMapper): ErrorMapper

    @Binds
    abstract fun bindAnimeRemoteDataSource(animeRemoteDataSourceImpl: AnimeRemoteDataSourceImpl): AnimeRemoteDataSource

    @Binds
    abstract fun bindAnimeRepository(animeRepositoryImpl: AnimeRepositoryImpl): AnimeRepository

    @Binds
    @IntoMap
    @ViewModelKey(AnimeViewModel::class)
    abstract fun bindViewModel(viewModel: AnimeViewModel): ViewModel

}
