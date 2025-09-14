package com.dezdeqness.di.modules

import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.domain.usecases.BaseListableUseCase
import com.dezdeqness.domain.usecases.GetChronologyUseCase
import com.dezdeqness.presentation.features.animechronology.AnimeChronologyUiMapper
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableUiMapper
import dagger.Module
import dagger.Provides

@Module(includes = [AnimeModule::class])
class AnimeChronologyModule {

    @Provides
    fun provideAnimeSimilarUiMapper(): GenericListableUiMapper =
        AnimeChronologyUiMapper()

    @Provides
    fun provideGetSimilarListUseCase(animeRepository: AnimeRepository): BaseListableUseCase =
        GetChronologyUseCase(animeRepository = animeRepository)

}
