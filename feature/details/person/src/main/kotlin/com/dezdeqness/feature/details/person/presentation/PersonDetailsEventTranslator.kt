package com.dezdeqness.feature.details.person.presentation

import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.DetailsEventTranslator
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace
import javax.inject.Inject

class PersonDetailsEventTranslator @Inject constructor() :
    DetailsEventTranslator<PersonDetailsUiEvent, PersonDetailsNamespace.Event> {

    override fun translate(uiEvent: PersonDetailsUiEvent): PersonDetailsNamespace.Event =
        when (uiEvent) {
            is PersonDetailsUiEvent.Base -> PersonDetailsNamespace.Event.Base(
                when (uiEvent.event) {
                    DetailsBaseUiEvent.SharePressed -> BaseDetailsEvent.SharePressed
                    DetailsBaseUiEvent.RetryClicked -> BaseDetailsEvent.RetryClicked
                    DetailsBaseUiEvent.FavouriteToggled -> BaseDetailsEvent.FavouriteToggleClicked
                },
            )
        }
}
