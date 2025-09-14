package com.dezdeqness.presentation.features.settings

import com.dezdeqness.contract.settings.models.InitialSection
import com.dezdeqness.presentation.models.RibbonStatusUiModel

interface SettingActions {
    fun onBackPressed()
    fun onNightThemeToggleClicked(isChecked: Boolean)

    fun onChangeInitialSectionClicked()
    fun onSelectedSectionChanged(section: InitialSection)
    fun onSelectedSectionDialogClosed()

    fun onChangeRibbonStatusClicked()
    fun onSelectedRibbonDataChanged(statuses: List<RibbonStatusUiModel>)
    fun onChangeRibbonStatusClosed()

    fun onNotificationToggleClicked(isEnabled: Boolean)

    fun onNotificationTimePickerClicked()
    fun onNotificationTimeSaved(hours: Int, minutes: Int)
    fun onNotificationTimePickerClosed()

    fun onMaxImageCacheSizeClicked()
    fun onMaxImageCacheSize(size: Int)
    fun onMaxImageCacheSizeDialogClosed()

    fun invalidate()

    fun onDebugOptionsClicked()
}
