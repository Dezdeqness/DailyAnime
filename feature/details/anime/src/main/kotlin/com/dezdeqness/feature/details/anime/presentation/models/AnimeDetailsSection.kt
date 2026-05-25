package com.dezdeqness.feature.details.anime.presentation.models

import com.dezdeqness.foundation.ui.views.details.BriefInfoEntry

sealed interface AnimeDetailsSection {
    data class Header(val imageUrl: String, val rating: Float?) : AnimeDetailsSection
    data class Title(val text: String) : AnimeDetailsSection
    data class BriefInfo(val items: List<BriefInfoEntry>) : AnimeDetailsSection
    data class Genres(val items: List<GenreChip>) : AnimeDetailsSection
    data class Description(val html: String) : AnimeDetailsSection
    data object MoreInfo : AnimeDetailsSection
    data class Related(val items: List<RelatedItem>) : AnimeDetailsSection
    data class Characters(val items: List<CharacterItem>) : AnimeDetailsSection
    data class Screenshots(val items: List<ScreenshotItem>) : AnimeDetailsSection
    data class Videos(val items: List<VideoItem>) : AnimeDetailsSection
    data object BottomSpacer : AnimeDetailsSection
}

data class GenreChip(val id: String, val name: String)

data class RelatedItem(
    val id: Long,
    val imageUrl: String,
    val type: String,
    val briefInfo: String,
)

data class CharacterItem(
    val id: Long,
    val name: String,
    val imageUrl: String,
)

data class ScreenshotItem(
    val previewUrl: String,
    val originalUrl: String,
)

data class VideoItem(
    val imageUrl: String,
    val name: String,
    val sourceUrl: String,
)
