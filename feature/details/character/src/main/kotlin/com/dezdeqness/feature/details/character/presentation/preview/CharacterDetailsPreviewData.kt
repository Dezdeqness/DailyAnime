package com.dezdeqness.feature.details.character.presentation.preview

import com.dezdeqness.feature.details.character.presentation.models.AnimeItem
import com.dezdeqness.feature.details.character.presentation.models.CharacterDetailsSection
import com.dezdeqness.feature.details.character.presentation.models.SeyuItem
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.sections.BottomSpacerSection
import com.dezdeqness.feature.details.common.presentation.sections.DescriptionSection
import com.dezdeqness.feature.details.common.presentation.sections.HeaderSection
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection

object CharacterDetailsPreviewData {

    val seyu = listOf(
        SeyuItem(id = 1L, name = "Natsuki Hanae", imageUrl = ""),
        SeyuItem(id = 2L, name = "Akari Kito", imageUrl = ""),
        SeyuItem(id = 3L, name = "Hiro Shimono", imageUrl = ""),
    )

    val animes = listOf(
        AnimeItem(id = 1L, title = "Kimetsu no Yaiba", kind = "TV", imageUrl = ""),
        AnimeItem(id = 2L, title = "Mugen Train", kind = "Movie", imageUrl = ""),
        AnimeItem(id = 3L, title = "Entertainment District", kind = "TV", imageUrl = ""),
    )

    val sections: List<DetailsSection> = listOf(
        HeaderSection(imageUrl = ""),
        TitleSection(text = "Tanjiro Kamado"),
        DescriptionSection(html = "Старший сын в семье Камадо."),
        CharacterDetailsSection.Seyu(items = seyu),
        CharacterDetailsSection.Animes(items = animes),
        BottomSpacerSection,
    )
}
