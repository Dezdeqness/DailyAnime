package com.dezdeqness.feature.details.anime.presentation

import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.DetailsEventTranslator
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import javax.inject.Inject

class AnimeDetailsEventTranslator @Inject constructor() :
    DetailsEventTranslator<AnimeDetailsUiEvent, AnimeDetailsNamespace.Event> {

    override fun translate(uiEvent: AnimeDetailsUiEvent): AnimeDetailsNamespace.Event =
        when (uiEvent) {
            is AnimeDetailsUiEvent.Base -> AnimeDetailsNamespace.Event.Base(
                when (uiEvent.event) {
                    DetailsBaseUiEvent.SharePressed -> BaseDetailsEvent.SharePressed
                    DetailsBaseUiEvent.RetryClicked -> BaseDetailsEvent.RetryClicked
                },
            )

            AnimeDetailsUiEvent.EditRateClicked -> AnimeDetailsNamespace.Event.EditRateClicked
            AnimeDetailsUiEvent.EditRateClosed -> AnimeDetailsNamespace.Event.EditRateClosed
            is AnimeDetailsUiEvent.SaveUserRate ->
                AnimeDetailsNamespace.Event.SaveUserRate(uiEvent.userRate)

            AnimeDetailsUiEvent.StatsClicked -> AnimeDetailsNamespace.Event.StatsClicked
            AnimeDetailsUiEvent.SimilarClicked -> AnimeDetailsNamespace.Event.SimilarClicked
            AnimeDetailsUiEvent.ChronologyClicked -> AnimeDetailsNamespace.Event.ChronologyClicked
            is AnimeDetailsUiEvent.RelatedAnimeClicked ->
                AnimeDetailsNamespace.Event.RelatedClicked(uiEvent.animeId)

            is AnimeDetailsUiEvent.CharacterClicked ->
                AnimeDetailsNamespace.Event.CharacterClicked(uiEvent.characterId)

            is AnimeDetailsUiEvent.ScreenshotClicked ->
                AnimeDetailsNamespace.Event.ScreenshotClicked(uiEvent.previewUrl)

            is AnimeDetailsUiEvent.VideoClicked ->
                AnimeDetailsNamespace.Event.VideoClicked(uiEvent.url)
        }
}
