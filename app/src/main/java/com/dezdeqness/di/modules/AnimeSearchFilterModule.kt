package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.data.repository.SearchFilterRepositoryImpl
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.di.subcomponents.SearchFilterListModule
import com.dezdeqness.domain.repository.SearchFilterRepository
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterComposer
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterViewModel
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSeasonCellComposer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [SearchFilterListModule::class])
abstract class AnimeSearchFilterModule {

    companion object {

        @Provides
        fun provideAnimeSearchFilterComposer(
            resourceManager: ResourceProvider,
            animeSeasonCellComposer: AnimeSeasonCellComposer,
        ) =
            AnimeSearchFilterComposer(
                resourceManager = resourceManager,
                animeSeasonCellComposer = animeSeasonCellComposer,
            )

        @Provides
        fun providesAnimeSeasonCellComposer(
            resourceManager: ResourceProvider,
        ) = AnimeSeasonCellComposer(resourceManager)

    }

    @Binds
    abstract fun bindSearchFilterRepository(searchFilterRepository: SearchFilterRepositoryImpl): SearchFilterRepository

    @Binds
    @IntoMap
    @ViewModelKey(AnimeSearchFilterViewModel::class)
    abstract fun bindAnimeSearchFilterViewModel(viewModel: AnimeSearchFilterViewModel): ViewModel

}
