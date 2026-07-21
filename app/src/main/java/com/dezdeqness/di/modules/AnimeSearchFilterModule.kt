package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.repository.SearchFilterRepositoryImpl
import com.dezdeqness.domain.repository.SearchFilterRepository
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilterComposer
import com.dezdeqness.feature.searchfilter.presentation.AnimeSearchFilterViewModel
import com.dezdeqness.feature.searchfilter.presentation.AnimeSeasonCellComposer
import com.dezdeqness.foundation.di.ViewModelKey
import com.dezdeqness.foundation.provider.ResourceProvider
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
            animeSeasonCellComposer: AnimeSeasonCellComposer,
        ) = AnimeSearchFilterComposer(
            resourceManager = resourceManager,
            animeSeasonCellComposer = animeSeasonCellComposer,
        )

        @Provides
        fun providesAnimeSeasonCellComposer(resourceManager: ResourceProvider) =
            AnimeSeasonCellComposer(resourceManager)
    }

    @Binds
    abstract fun bindSearchFilterRepository(searchFilterRepository: SearchFilterRepositoryImpl): SearchFilterRepository

    @Binds
    @IntoMap
    @ViewModelKey(AnimeSearchFilterViewModel::class)
    abstract fun bindAnimeSearchFilterViewModel(viewModel: AnimeSearchFilterViewModel): ViewModel
}
