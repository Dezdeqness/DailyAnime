package com.dezdeqness.presentation.features.settings

import com.dezdeqness.core.ui.TimeData
import com.dezdeqness.presentation.features.settings.composables.SelectSectionItem
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.google.common.collect.ImmutableList

data class SettingsState(
    val isDarkThemeEnabled: Boolean = false,
    val selectedSection: SelectSectionItem = SelectSectionItem.getSections().first(),
    val personalRibbonStatuses: ImmutableList<RibbonStatusUiModel> = ImmutableList.of(),
    val isSelectInitialSectionDialogShown: Boolean = false,
    val isStatusReorderDialogShown: Boolean = false,
    val isAuthorized: Boolean = false,
    val isNotificationsTurnOn: Boolean = false,
    val isNotificationsEnabled: Boolean = false,
    val isNotificationTimePickerDialogShown: Boolean = false,
    val notificationTimeData: TimeData = TimeData(hours = 19, minutes = 0)
) {
    val ribbonSettingsSubTitle: String
        get() = personalRibbonStatuses.joinToString(", ") { it.displayName }
}