package com.dezdeqness.presentation.features.authorization

import com.dezdeqness.presentation.event.Event

data class AuthorizationState(
    val url: String = "",
    val isLoading: Boolean = false,
    val events: List<Event> = listOf(),
)
