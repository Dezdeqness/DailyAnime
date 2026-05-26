package com.dezdeqness.feature.details.anime.presentation.models

import com.dezdeqness.feature.details.common.presentation.DetailsSection

sealed interface AnimeDetailsSection : DetailsSection {
    data class Genres(val items: List<GenreChip>) : AnimeDetailsSection {
        override val rendererType: String = TYPE

        companion object {
            const val TYPE = "anime_genres"
        }
    }

    data object MoreInfo : AnimeDetailsSection {
        override val rendererType: String = TYPE
        const val TYPE = "anime_more_info"
    }

    data class Related(val items: List<RelatedItem>) : AnimeDetailsSection {
        override val rendererType: String = TYPE

        companion object {
            const val TYPE = "anime_related"
        }
    }

    data class Characters(val items: List<CharacterItem>) : AnimeDetailsSection {
        override val rendererType: String = TYPE

        companion object {
            const val TYPE = "anime_characters"
        }
    }

    data class Screenshots(val items: List<ScreenshotItem>) : AnimeDetailsSection {
        override val rendererType: String = TYPE

        companion object {
            const val TYPE = "anime_screenshots"
        }
    }

    data class Videos(val items: List<VideoItem>) : AnimeDetailsSection {
        override val rendererType: String = TYPE

        companion object {
            const val TYPE = "anime_videos"
        }
    }
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
