package com.dezdeqness.feature.details.common.presentation.store

import com.dezdeqness.contract.favourite.model.FavouriteButtonState

sealed interface BaseDetailsEvent {
    data class InitialLoad(val id: Long) : BaseDetailsEvent
    data object RetryClicked : BaseDetailsEvent
    data class OnDetailsLoadError(val message: String, val error: Throwable) : BaseDetailsEvent
    data object SharePressed : BaseDetailsEvent

    data object FavouriteToggleClicked : BaseDetailsEvent
    data class FavouriteStatusChanged(val state: FavouriteButtonState) : BaseDetailsEvent
    data object FavouriteToggleSucceeded : BaseDetailsEvent
    data class FavouriteToggleFailed(val error: Throwable) : BaseDetailsEvent
}
