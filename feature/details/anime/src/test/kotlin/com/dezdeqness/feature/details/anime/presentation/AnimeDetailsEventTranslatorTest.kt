package com.dezdeqness.feature.details.anime.presentation

import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.userrate.EditRateUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimeDetailsEventTranslatorTest {

    private val translator = AnimeDetailsEventTranslator()

    @Test
    fun `WHEN Base SharePressed SHOULD translate to Base SharePressed event`() {
        val event = translator.translate(AnimeDetailsUiEvent.Base(DetailsBaseUiEvent.SharePressed))
        assertEquals(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed), event)
    }

    @Test
    fun `WHEN Base RetryClicked SHOULD translate to Base RetryClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.Base(DetailsBaseUiEvent.RetryClicked))
        assertEquals(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.RetryClicked), event)
    }

    @Test
    fun `WHEN EditRateClicked SHOULD translate to EditRateClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.EditRateClicked)
        assertEquals(AnimeDetailsNamespace.Event.EditRateClicked, event)
    }

    @Test
    fun `WHEN EditRateClosed SHOULD translate to EditRateClosed event`() {
        val event = translator.translate(AnimeDetailsUiEvent.EditRateClosed)
        assertEquals(AnimeDetailsNamespace.Event.EditRateClosed, event)
    }

    @Test
    fun `WHEN SaveUserRate SHOULD translate to SaveUserRate event`() {
        val model = EditRateUiModel(
            rateId = 1L,
            status = "watching",
            episodes = 3L,
            score = 7f,
            comment = "",
        )
        val event = translator.translate(AnimeDetailsUiEvent.SaveUserRate(model))
        assertEquals(AnimeDetailsNamespace.Event.SaveUserRate(model), event)
    }

    @Test
    fun `WHEN StatsClicked SHOULD translate to StatsClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.StatsClicked)
        assertEquals(AnimeDetailsNamespace.Event.StatsClicked, event)
    }

    @Test
    fun `WHEN SimilarClicked SHOULD translate to SimilarClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.SimilarClicked)
        assertEquals(AnimeDetailsNamespace.Event.SimilarClicked, event)
    }

    @Test
    fun `WHEN ChronologyClicked SHOULD translate to ChronologyClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.ChronologyClicked)
        assertEquals(AnimeDetailsNamespace.Event.ChronologyClicked, event)
    }

    @Test
    fun `WHEN RelatedAnimeClicked SHOULD translate to RelatedClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.RelatedAnimeClicked(animeId = 99L))
        assertEquals(AnimeDetailsNamespace.Event.RelatedClicked(animeId = 99L), event)
    }

    @Test
    fun `WHEN CharacterClicked SHOULD translate to CharacterClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.CharacterClicked(characterId = 11L))
        assertEquals(AnimeDetailsNamespace.Event.CharacterClicked(characterId = 11L), event)
    }

    @Test
    fun `WHEN ScreenshotClicked SHOULD translate to ScreenshotClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.ScreenshotClicked(previewUrl = "url"))
        assertEquals(AnimeDetailsNamespace.Event.ScreenshotClicked(previewUrl = "url"), event)
    }

    @Test
    fun `WHEN VideoClicked SHOULD translate to VideoClicked event`() {
        val event = translator.translate(AnimeDetailsUiEvent.VideoClicked(url = "https://video"))
        assertEquals(AnimeDetailsNamespace.Event.VideoClicked(url = "https://video"), event)
    }
}
