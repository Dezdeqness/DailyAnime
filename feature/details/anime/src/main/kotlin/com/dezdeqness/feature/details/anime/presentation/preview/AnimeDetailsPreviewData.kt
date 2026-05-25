package com.dezdeqness.feature.details.anime.presentation.preview

import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.anime.presentation.models.CharacterItem
import com.dezdeqness.feature.details.anime.presentation.models.GenreChip
import com.dezdeqness.feature.details.anime.presentation.models.RelatedItem
import com.dezdeqness.feature.details.anime.presentation.models.ScreenshotItem
import com.dezdeqness.feature.details.anime.presentation.models.VideoItem
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

    val sections: List<AnimeDetailsSection> = listOf(
        AnimeDetailsSection.Header(imageUrl = "", rating = 8.7f),
        AnimeDetailsSection.Title(text = "Клинок, рассекающий демонов"),
        AnimeDetailsSection.BriefInfo(items = briefInfo),
        AnimeDetailsSection.Genres(items = genres),
        AnimeDetailsSection.Description(html = "Япония эпохи Тайсё. Тандзиро Камадо живёт с семьёй высоко в горах."),
        AnimeDetailsSection.MoreInfo,
        AnimeDetailsSection.Related(items = related),
        AnimeDetailsSection.Characters(items = characters),
        AnimeDetailsSection.Screenshots(items = screenshots),
        AnimeDetailsSection.Videos(items = videos),
        AnimeDetailsSection.BottomSpacer,
    )
}
