package com.dezdeqness.feature.details.character.presentation

import com.dezdeqness.feature.details.character.presentation.store.CharacterDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.DetailsEventTranslator
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import javax.inject.Inject

class CharacterDetailsEventTranslator @Inject constructor() :
    DetailsEventTranslator<CharacterDetailsUiEvent, CharacterDetailsNamespace.Event> {

    override fun translate(uiEvent: CharacterDetailsUiEvent): CharacterDetailsNamespace.Event = when (uiEvent) {
        is CharacterDetailsUiEvent.Base -> CharacterDetailsNamespace.Event.Base(
            when (uiEvent.event) {
                DetailsBaseUiEvent.SharePressed -> BaseDetailsEvent.SharePressed
                DetailsBaseUiEvent.RetryClicked -> BaseDetailsEvent.RetryClicked
                DetailsBaseUiEvent.FavouriteToggled -> BaseDetailsEvent.FavouriteToggleClicked
            },
        )

        is CharacterDetailsUiEvent.SeyuClicked ->
            CharacterDetailsNamespace.Event.SeyuClicked(uiEvent.personId)

        is CharacterDetailsUiEvent.AnimeClicked ->
            CharacterDetailsNamespace.Event.AnimeClicked(uiEvent.animeId)
    }
}
