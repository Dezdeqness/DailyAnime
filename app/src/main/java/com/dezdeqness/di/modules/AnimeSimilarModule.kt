package com.dezdeqness.di.modules

import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.domain.usecases.BaseListableUseCase
import com.dezdeqness.domain.usecases.GetSimilarListUseCase
import com.dezdeqness.presentation.features.animesimilar.AnimeSimilarUiMapper
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableUiMapper
import com.dezdeqness.utils.AnimeKindUtils
import com.dezdeqness.utils.ImageUrlUtils
import dagger.Module
import dagger.Provides

@Module(includes = [AnimeModule::class])
class AnimeSimilarModule {

    @Provides
    fun provideAnimeSimilarUiMapper(
        imageUrlUtils: ImageUrlUtils,
        animeKindUtils: AnimeKindUtils,
    ): GenericListableUiMapper =
        AnimeSimilarUiMapper(
            imageUrlUtils = imageUrlUtils,
            animeKindUtils = animeKindUtils,
        )

    @Provides
    fun provideGetSimilarListUseCase(animeRepository: AnimeRepository): BaseListableUseCase =
        GetSimilarListUseCase(animeRepository = animeRepository)

}
