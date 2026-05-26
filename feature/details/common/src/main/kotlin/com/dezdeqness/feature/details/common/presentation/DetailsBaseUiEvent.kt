package com.dezdeqness.feature.details.common.presentation

sealed interface DetailsBaseUiEvent {
    data object SharePressed : DetailsBaseUiEvent
    data object RetryClicked : DetailsBaseUiEvent
}
