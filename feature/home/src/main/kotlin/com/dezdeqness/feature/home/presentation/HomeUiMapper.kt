package com.dezdeqness.feature.home.presentation

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.domain.model.HomeCalendarEntity
import com.dezdeqness.feature.home.presentation.models.HomeCalendarUiModel
import com.dezdeqness.feature.home.presentation.models.SectionAnimeUiModel
import javax.inject.Inject

class HomeUiMapper @Inject constructor() {

    fun mapSectionAnimeModel(animeBriefEntity: AnimeBriefEntity) = SectionAnimeUiModel(
        id = animeBriefEntity.id,
        title = animeBriefEntity.takeIf { it.russian.isNotEmpty() }?.russian
            ?: animeBriefEntity.name,
        logoUrl = animeBriefEntity.image.original,
    )

    fun mapHomeCalendarAnimeModel(homeCalendarEntity: HomeCalendarEntity) = HomeCalendarUiModel(
        id = homeCalendarEntity.id,
        title = homeCalendarEntity.takeIf { it.russian.isNotEmpty() }?.russian
            ?: homeCalendarEntity.name,
        description = HtmlCompat.fromHtml(
            homeCalendarEntity.description.orEmpty(),
            FROM_HTML_MODE_COMPACT,
        ).toString(),
        imageUrl = homeCalendarEntity.image.original,
    )
}
