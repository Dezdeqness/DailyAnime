package com.dezdeqness.presentation.features.settings

import com.dezdeqness.presentation.event.Event

data class SettingsState(
    val isDarkThemeEnabled: Boolean = false,
    val events: List<Event> = listOf(),
)
