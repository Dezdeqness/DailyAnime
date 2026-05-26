package com.dezdeqness.feature.details.person.presentation

import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonDetailsEventTranslatorTest {

    private val translator = PersonDetailsEventTranslator()

    @Test
    fun `WHEN Base SharePressed SHOULD translate to Base SharePressed event`() {
        val event = translator.translate(PersonDetailsUiEvent.Base(DetailsBaseUiEvent.SharePressed))
        assertEquals(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed), event)
    }

    @Test
    fun `WHEN Base RetryClicked SHOULD translate to Base RetryClicked event`() {
        val event = translator.translate(PersonDetailsUiEvent.Base(DetailsBaseUiEvent.RetryClicked))
        assertEquals(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.RetryClicked), event)
    }
}
