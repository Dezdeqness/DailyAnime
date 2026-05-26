package com.dezdeqness.feature.details.character.presentation

import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent

sealed interface CharacterDetailsUiEvent {
    data class Base(val event: DetailsBaseUiEvent) : CharacterDetailsUiEvent

    data class SeyuClicked(val personId: Long) : CharacterDetailsUiEvent
    data class AnimeClicked(val animeId: Long) : CharacterDetailsUiEvent
}
