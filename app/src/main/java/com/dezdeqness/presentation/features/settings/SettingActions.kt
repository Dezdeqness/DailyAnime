package com.dezdeqness.presentation.features.settings

import com.dezdeqness.contract.settings.models.InitialSection
import com.dezdeqness.contract.settings.models.ThemeMode
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel

interface SettingActions {
    fun onBackPressed()

    fun onThemeClicked()
    fun onThemeSelected(mode: ThemeMode)
    fun onThemeDialogClosed()

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

    fun onChangeAdultContentClicked(isEnabled: Boolean)

    fun invalidate()

    fun onDebugOptionsClicked()
    fun onSelectInterestsClicked()
}
