package com.dezdeqness.feature.details.person.presentation

import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent

sealed interface PersonDetailsUiEvent {
    data class Base(val event: DetailsBaseUiEvent) : PersonDetailsUiEvent
}
