package com.dezdeqness.presentation

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.domain.model.HomeCalendarEntity
import com.dezdeqness.presentation.features.animelist.AnimeUiModel
import com.dezdeqness.presentation.features.home.model.HomeCalendarUiModel
import com.dezdeqness.presentation.features.home.model.SectionAnimeUiModel
import com.dezdeqness.presentation.models.RelatedItemUiModel
import com.dezdeqness.utils.AnimeKindUtils
import com.dezdeqness.utils.ImageUrlUtils
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class AnimeUiMapper @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
    private val animeKindUtils: AnimeKindUtils,
) {

    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    fun map(items: List<AnimeBriefEntity>) = items.map(::mapAnimeBrief)

    fun mapAnimeBrief(animeBriefEntity: AnimeBriefEntity) =
        AnimeUiModel(
            id = animeBriefEntity.id,
            title = animeBriefEntity.takeIf { it.russian.isNotEmpty() }?.russian
                ?: animeBriefEntity.name,
            kind = animeKindUtils.mapKind(animeBriefEntity.kind),
            logoUrl = imageUrlUtils.getImageWithBaseUrl(animeBriefEntity.image.original),
        )

    fun map(relatedItemUiModel: RelatedItemEntity) =
        RelatedItemUiModel(
            id = relatedItemUiModel.animeBriefEntity.id,
            type = relatedItemUiModel.takeIf { it.relationTitleRussian.isNotEmpty() }?.relationTitleRussian
                ?: relatedItemUiModel.relationTitle,
            briefInfo =
                relatedItemUiModel.animeBriefEntity.kind.name
                        + " • "
                        + yearFormatter.format(relatedItemUiModel.animeBriefEntity.airedOnTimestamp),
            logoUrl = relatedItemUiModel.animeBriefEntity.image.original,
        )

    fun mapSectionAnimeModel(animeBriefEntity: AnimeBriefEntity) =
        SectionAnimeUiModel(
            id = animeBriefEntity.id,
            title = animeBriefEntity.takeIf { it.russian.isNotEmpty() }?.russian
                ?: animeBriefEntity.name,
            logoUrl = animeBriefEntity.image.preview,
        )

    fun mapHomeCalendarAnimeModel(homeCalendarEntity: HomeCalendarEntity) =
        HomeCalendarUiModel(
            id = homeCalendarEntity.id,
            title = homeCalendarEntity.takeIf { it.russian.isNotEmpty() }?.russian
                ?: homeCalendarEntity.name,
            description = HtmlCompat.fromHtml(
                homeCalendarEntity.description.orEmpty(),
                FROM_HTML_MODE_COMPACT
            ).toString(),
            imageUrl = homeCalendarEntity.image.original,
        )

}
