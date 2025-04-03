package com.dezdeqness.presentation.features.settings

import com.dezdeqness.presentation.models.RibbonStatusUiModel

interface SettingActions {
    fun onBackPressed()
    fun onNightThemeToggleClicked(isChecked: Boolean)

    fun onChangeInitialSectionClicked()
    fun onSelectedSectionChanged(sectionId: Int)
    fun onSelectedSectionDialogClosed()

    fun onChangeRibbonStatusClicked()
    fun onSelectedRibbonDataChanged(statuses: List<RibbonStatusUiModel>)
    fun onChangeRibbonStatusClosed()

    fun onNotificationToggleClicked(isEnabled: Boolean)

    fun onNotificationTimePickerClicked()
    fun onNotificationTimeSaved(hours: Int, minutes: Int)
    fun onNotificationTimePickerClosed()
}