package com.dezdeqness.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterComposer
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class AnimeSearchFilterModule {

    companion object {

        @Provides
        fun provideAnimeSearchFilterComposer(
            configurationProvider: ConfigurationProvider,
            resourceManager: ResourceProvider,
        ) =
            AnimeSearchFilterComposer(
                configurationProvider = configurationProvider,
                resourceManager = resourceManager,
            )
    }

//    @Binds
//    abstract fun bindAnimeSearchFilterComposer(composer: AnimeSearchFilterComposer): AnimeSearchFilterComposer

    @Binds
    @IntoMap
    @ViewModelKey(AnimeSearchFilterViewModel::class)
    abstract fun bindAnimeSearchFilterViewModel(viewModel: AnimeSearchFilterViewModel): ViewModel

}