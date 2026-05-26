package com.dezdeqness.feature.details.character.presentation.composer

import com.dezdeqness.domain.model.CharacterDetailsEntity
import com.dezdeqness.feature.details.character.presentation.models.AnimeItem
import com.dezdeqness.feature.details.character.presentation.models.CharacterDetailsSection
import com.dezdeqness.feature.details.character.presentation.models.SeyuItem
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.sections.BottomSpacerSection
import com.dezdeqness.feature.details.common.presentation.sections.DescriptionSection
import com.dezdeqness.feature.details.common.presentation.sections.HeaderSection
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import com.dezdeqness.shared.presentation.utils.UrlNormalizer
import javax.inject.Inject

class CharacterDetailsComposer @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
    private val urlNormalizer: UrlNormalizer,
) {

    fun compose(entity: CharacterDetailsEntity): List<DetailsSection> {
        val sections = mutableListOf<DetailsSection>()

        sections += HeaderSection(imageUrl = urlNormalizer.normalize(entity.image.original))
        sections += TitleSection(text = entity.russian.ifEmpty { entity.name })

        if (entity.description != null) {
            sections += DescriptionSection(html = entity.descriptionHTML)
        }

        entity.seyuList
            .map {
                SeyuItem(
                    id = it.id,
                    name = it.russian.ifEmpty { it.name },
                    imageUrl = urlNormalizer.normalize(it.image.preview),
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let { sections += CharacterDetailsSection.Seyu(items = it) }

        entity.animeList
            .map {
                AnimeItem(
                    id = it.id,
                    title = it.russian.ifEmpty { it.name },
                    kind = animeKindUtils.mapKind(it.kind),
                    imageUrl = urlNormalizer.normalize(it.image.original),
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let { sections += CharacterDetailsSection.Animes(items = it) }

        sections += BottomSpacerSection

        return sections
    }
}
