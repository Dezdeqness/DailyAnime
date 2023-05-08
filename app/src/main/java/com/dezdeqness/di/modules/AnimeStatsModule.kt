package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.di.subcomponents.AnimeStatsArgsModule
import com.dezdeqness.presentation.features.animestats.AnimeStatsComposer
import com.dezdeqness.presentation.features.animestats.AnimeStatsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [AnimeStatsArgsModule::class])
abstract class AnimeStatsModule {

    companion object {
        @Provides
        fun provideAnimeStatsComposer(
            resourceProvider: ResourceProvider,
        ) = AnimeStatsComposer(
            resourceProvider = resourceProvider,
        )
    }

    @Binds
    @IntoMap
    @ViewModelKey(AnimeStatsViewModel::class)
    abstract fun bindAnimeStatsViewModel(viewModel: AnimeStatsViewModel): ViewModel

}
