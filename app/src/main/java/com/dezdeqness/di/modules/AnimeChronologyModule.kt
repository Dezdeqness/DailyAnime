package com.dezdeqness.di.modules

import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.domain.usecases.BaseListableUseCase
import com.dezdeqness.domain.usecases.GetChronologyUseCase
import com.dezdeqness.presentation.features.animechronology.AnimeChronologyUiMapper
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableUiMapper
import com.dezdeqness.utils.AnimeKindUtils
import dagger.Module
import dagger.Provides

@Module(includes = [AnimeModule::class])
class AnimeChronologyModule {

    @Provides
    fun provideAnimeSimilarUiMapper(animeKindUtils: AnimeKindUtils): GenericListableUiMapper =
        AnimeChronologyUiMapper(animeKindUtils = animeKindUtils)

    @Provides
    fun provideGetSimilarListUseCase(animeRepository: AnimeRepository): BaseListableUseCase =
        GetChronologyUseCase(animeRepository = animeRepository)

}
