package com.dezdeqness.presentation.features.personallist.host

import com.dezdeqness.presentation.event.Event

data class PersonalListHostState(
    val isAuthorized: Boolean = false,
    val events: List<Event> = listOf(),
)
