package com.dezdeqness.feature.details.common.presentation.store

sealed interface BaseDetailsEvent {
    data class InitialLoad(val id: Long) : BaseDetailsEvent
    data object RetryClicked : BaseDetailsEvent
    data class OnDetailsLoadError(val message: String, val error: Throwable) : BaseDetailsEvent
    data object SharePressed : BaseDetailsEvent
}
