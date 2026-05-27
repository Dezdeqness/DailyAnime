package com.dezdeqness.feature.details.character.presentation

import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CharacterDetailsEventTranslatorTest {

    private val translator = CharacterDetailsEventTranslator()

    @Test
    fun `WHEN Base SharePressed SHOULD translate to Base SharePressed event`() {
        val event = translator.translate(
            CharacterDetailsUiEvent.Base(DetailsBaseUiEvent.SharePressed),
        )
        assertEquals(CharacterDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed), event)
    }

    @Test
    fun `WHEN Base RetryClicked SHOULD translate to Base RetryClicked event`() {
        val event = translator.translate(
            CharacterDetailsUiEvent.Base(DetailsBaseUiEvent.RetryClicked),
        )
        assertEquals(CharacterDetailsNamespace.Event.Base(BaseDetailsEvent.RetryClicked), event)
    }

    @Test
    fun `WHEN SeyuClicked SHOULD translate to SeyuClicked event`() {
        val event = translator.translate(CharacterDetailsUiEvent.SeyuClicked(personId = 77L))
        assertEquals(CharacterDetailsNamespace.Event.SeyuClicked(personId = 77L), event)
    }

    @Test
    fun `WHEN AnimeClicked SHOULD translate to AnimeClicked event`() {
        val event = translator.translate(CharacterDetailsUiEvent.AnimeClicked(animeId = 88L))
        assertEquals(CharacterDetailsNamespace.Event.AnimeClicked(animeId = 88L), event)
    }
}
