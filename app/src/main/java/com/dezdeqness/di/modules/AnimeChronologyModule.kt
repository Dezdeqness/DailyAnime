package com.dezdeqness.di.modules

import com.dezdeqness.contract.anime.repository.AnimeChronologyRepository
import com.dezdeqness.domain.anime.usecases.BaseListableUseCase
import com.dezdeqness.domain.anime.usecases.GetChronologyUseCase
import com.dezdeqness.feature.details.related.presentation.RelatedListUiMapper
import com.dezdeqness.feature.details.related.presentation.mapper.ChronologyUiMapper
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import dagger.Module
import dagger.Provides

@Module(includes = [AnimeModule::class])
class AnimeChronologyModule {

    @Provides
    fun provideChronologyUiMapper(animeKindUtils: AnimeKindUtils): RelatedListUiMapper =
        ChronologyUiMapper(animeKindUtils = animeKindUtils)

    @Provides
    fun provideGetChronologyUseCase(repository: AnimeChronologyRepository): BaseListableUseCase =
        GetChronologyUseCase(repository = repository)
}
