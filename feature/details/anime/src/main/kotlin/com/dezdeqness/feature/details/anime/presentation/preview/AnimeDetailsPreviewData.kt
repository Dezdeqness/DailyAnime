package com.dezdeqness.feature.details.anime.presentation.preview

import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.anime.presentation.models.CharacterItem
import com.dezdeqness.feature.details.anime.presentation.models.GenreChip
import com.dezdeqness.feature.details.anime.presentation.models.RelatedItem
import com.dezdeqness.feature.details.anime.presentation.models.ScreenshotItem
import com.dezdeqness.feature.details.anime.presentation.models.VideoItem
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.sections.BottomSpacerSection
import com.dezdeqness.feature.details.common.presentation.sections.BriefInfoSection
import com.dezdeqness.feature.details.common.presentation.sections.DescriptionSection
import com.dezdeqness.feature.details.common.presentation.sections.HeaderSection
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection
import com.dezdeqness.foundation.ui.views.details.BriefInfoEntry

object AnimeDetailsPreviewData {

    val genres = listOf(
        GenreChip(id = "1", name = "Сёнен"),
        GenreChip(id = "2", name = "Приключения"),
        GenreChip(id = "3", name = "Фэнтези"),
        GenreChip(id = "4", name = "Драма"),
    )

    val related = listOf(
        RelatedItem(id = 1L, imageUrl = "", type = "Sequel", briefInfo = "TV · 2024"),
        RelatedItem(id = 2L, imageUrl = "", type = "Prequel", briefInfo = "TV · 2018"),
        RelatedItem(id = 3L, imageUrl = "", type = "Side story", briefInfo = "OVA · 2020"),
    )

    val characters = listOf(
        CharacterItem(id = 1L, name = "Tanjiro Kamado", imageUrl = ""),
        CharacterItem(id = 2L, name = "Nezuko Kamado", imageUrl = ""),
        CharacterItem(id = 3L, name = "Zenitsu Agatsuma", imageUrl = ""),
        CharacterItem(id = 4L, name = "Inosuke Hashibira", imageUrl = ""),
    )

    val screenshots = listOf(
        ScreenshotItem(previewUrl = "", originalUrl = ""),
        ScreenshotItem(previewUrl = "", originalUrl = ""),
        ScreenshotItem(previewUrl = "", originalUrl = ""),
    )

    val videos = listOf(
        VideoItem(imageUrl = "", name = "Opening 1", sourceUrl = ""),
        VideoItem(imageUrl = "", name = "Ending 1", sourceUrl = ""),
        VideoItem(imageUrl = "", name = "PV", sourceUrl = ""),
    )

    val briefInfo = listOf(
        BriefInfoEntry(title = "Статус", info = "Вышел"),
        BriefInfoEntry(title = "Эпизоды", info = "26 / 26"),
        BriefInfoEntry(title = "Тип", info = "TV"),
        BriefInfoEntry(title = "Возраст", info = "PG-13"),
    )

    val sections: List<DetailsSection> = listOf(
        HeaderSection(imageUrl = "", rating = 8.7f),
        TitleSection(text = "Клинок, рассекающий демонов"),
        BriefInfoSection(items = briefInfo),
        AnimeDetailsSection.Genres(items = genres),
        DescriptionSection(html = "Япония эпохи Тайсё. Тандзиро Камадо живёт с семьёй высоко в горах."),
        AnimeDetailsSection.MoreInfo(
            isSimilarVisible = true,
            isChronologyVisible = true,
            isStatsVisible = true,
        ),
        AnimeDetailsSection.Related(items = related),
        AnimeDetailsSection.Characters(items = characters, isClickable = true),
        AnimeDetailsSection.Screenshots(items = screenshots),
        AnimeDetailsSection.Videos(items = videos),
        BottomSpacerSection,
    )

    val anilistSections: List<DetailsSection> = listOf(
        HeaderSection(imageUrl = "", rating = 8.3f),
        TitleSection(text = "Kimetsu no Yaiba"),
        BriefInfoSection(
            items = listOf(
                BriefInfoEntry(title = "Статус", info = "Вышел"),
                BriefInfoEntry(title = "Тип", info = "TV"),
                BriefInfoEntry(title = "Эпизоды", info = "26"),
                BriefInfoEntry(title = "Длительность эпизода", info = "24 мин."),
            ),
        ),
        AnimeDetailsSection.Genres(
            items = listOf(
                GenreChip(id = "Action", name = "Action"),
                GenreChip(id = "Adventure", name = "Adventure"),
                GenreChip(id = "Drama", name = "Drama"),
                GenreChip(id = "Fantasy", name = "Fantasy"),
            ),
        ),
        DescriptionSection(
            html = "It is the Taisho Period in Japan. Tanjiro, a kindhearted boy, sells charcoal for a living.",
        ),
        AnimeDetailsSection.MoreInfo(
            isSimilarVisible = false,
            isChronologyVisible = false,
            isStatsVisible = true,
        ),
        AnimeDetailsSection.Related(
            items = listOf(
                RelatedItem(id = 1L, imageUrl = "", type = "Продолжение", briefInfo = "Фильм • 2020"),
                RelatedItem(id = 2L, imageUrl = "", type = "Адаптация", briefInfo = "TV"),
            ),
        ),
        AnimeDetailsSection.Characters(items = characters, isClickable = false),
        AnimeDetailsSection.Videos(
            items = listOf(VideoItem(imageUrl = "", name = "Трейлер", sourceUrl = "")),
        ),
        BottomSpacerSection,
    )
}
