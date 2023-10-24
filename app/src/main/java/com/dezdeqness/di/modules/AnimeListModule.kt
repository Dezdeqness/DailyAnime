package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.domain.usecases.GetAnimeListUseCase
import com.dezdeqness.presentation.features.animelist.AnimeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [AnimeModule::class])
abstract class AnimeListModule {

    companion object {

        @Provides
        fun provideGetAnimeListUseCase(animeRepository: AnimeRepository) = GetAnimeListUseCase(
            animeRepository = animeRepository
        )

    }

    @Binds
    @IntoMap
    @ViewModelKey(AnimeViewModel::class)
    abstract fun bindViewModel(viewModel: AnimeViewModel): ViewModel


}
