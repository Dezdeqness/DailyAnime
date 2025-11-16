package com.dezdeqness.feature.onboarding.selectgenres.presentation

import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.anime.model.GenreKindEntity
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel
import javax.inject.Inject

class SelectGenresMapper @Inject constructor() {

    fun map(item: GenreEntity) = GenreUiModel(
        id = item.numericId,
        name = item.name,
        isGenre = item.kind == GenreKindEntity.GENRE,
    )
}
