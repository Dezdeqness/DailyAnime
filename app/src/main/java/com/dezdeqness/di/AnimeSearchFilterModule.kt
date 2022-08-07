package com.dezdeqness.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.data.repository.SearchFilterRepositoryImpl
import com.dezdeqness.domain.repository.SearchFilterRepository
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterComposer
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class AnimeSearchFilterModule {

    companion object {

        @Provides
        fun provideAnimeSearchFilterComposer(
            resourceManager: ResourceProvider,
        ) =
            AnimeSearchFilterComposer(
                resourceManager = resourceManager,
            )
    }

    @Binds
    abstract fun bindSearchFilterRepository(searchFilterRepository: SearchFilterRepositoryImpl): SearchFilterRepository

    @Binds
    @IntoMap
    @ViewModelKey(AnimeSearchFilterViewModel::class)
    abstract fun bindAnimeSearchFilterViewModel(viewModel: AnimeSearchFilterViewModel): ViewModel

}