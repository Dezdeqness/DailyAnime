package com.dezdeqness.feature.details.anime.presentation

import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.userrate.EditRateUiModel

sealed interface AnimeDetailsUiEvent {
    data class Base(val event: DetailsBaseUiEvent) : AnimeDetailsUiEvent

    data object EditRateClicked : AnimeDetailsUiEvent
    data object EditRateClosed : AnimeDetailsUiEvent
    data class SaveUserRate(val userRate: EditRateUiModel) : AnimeDetailsUiEvent

    data object StatsClicked : AnimeDetailsUiEvent
    data object SimilarClicked : AnimeDetailsUiEvent
    data object ChronologyClicked : AnimeDetailsUiEvent
    data class RelatedAnimeClicked(val animeId: Long) : AnimeDetailsUiEvent
    data class CharacterClicked(val characterId: Long) : AnimeDetailsUiEvent
    data class ScreenshotClicked(val previewUrl: String) : AnimeDetailsUiEvent
    data class VideoClicked(val url: String) : AnimeDetailsUiEvent
}
