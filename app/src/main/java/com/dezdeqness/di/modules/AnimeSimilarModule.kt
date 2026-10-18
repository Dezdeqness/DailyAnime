package com.dezdeqness.di.modules

import com.dezdeqness.contract.anime.repository.AnimeSimilarRepository
import com.dezdeqness.data.utils.ImageUrlUtils
import com.dezdeqness.domain.anime.usecases.BaseListableUseCase
import com.dezdeqness.domain.anime.usecases.GetSimilarListUseCase
import com.dezdeqness.feature.details.related.presentation.RelatedListUiMapper
import com.dezdeqness.feature.details.related.presentation.mapper.SimilarUiMapper
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import dagger.Module
import dagger.Provides

@Module(includes = [AnimeModule::class])
class AnimeSimilarModule {

    @Provides
    fun provideSimilarUiMapper(
        imageUrlUtils: ImageUrlUtils,
        animeKindUtils: AnimeKindUtils,
    ): RelatedListUiMapper = SimilarUiMapper(
        imageUrlUtils = imageUrlUtils,
        animeKindUtils = animeKindUtils,
    )

    @Provides
    fun provideGetSimilarListUseCase(repository: AnimeSimilarRepository): BaseListableUseCase =
        GetSimilarListUseCase(repository = repository)
}
