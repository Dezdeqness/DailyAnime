package com.dezdeqness.presentation.features.settings

import com.dezdeqness.presentation.features.settings.composables.SelectSectionItem

data class SettingsState(
    val isDarkThemeEnabled: Boolean = false,
    val selectedSection: SelectSectionItem = SelectSectionItem.getSections().first(),
    val isSelectInitialSectionDialogShown: Boolean = false,
)
