package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.anime.usecases.GetAnimeListUseCase
import com.dezdeqness.domain.anime.usecases.GetAnimeListUseCaseImpl
import com.dezdeqness.feature.history.di.HistoryModule
import com.dezdeqness.feature.search.presentation.AnimeViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [AnimeModule::class, HistoryModule::class])
abstract class AnimeListModule {

    @Binds
    abstract fun bindGetAnimeListUseCase(getAnimeListUseCase: GetAnimeListUseCaseImpl): GetAnimeListUseCase

    @Binds
    @IntoMap
    @ViewModelKey(AnimeViewModel::class)
    abstract fun bindViewModel(viewModel: AnimeViewModel): ViewModel
}
