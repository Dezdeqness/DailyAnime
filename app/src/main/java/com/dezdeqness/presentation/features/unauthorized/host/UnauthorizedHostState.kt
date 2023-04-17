package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.presentation.event.Event

data class UnauthorizedHostState(
    val isAuthorized: Boolean = false,
    val events: List<Event> = listOf(),
)
