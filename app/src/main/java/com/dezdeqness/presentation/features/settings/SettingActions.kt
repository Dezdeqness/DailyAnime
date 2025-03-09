package com.dezdeqness.presentation.features.settings

interface SettingActions {
    fun onBackPressed()
    fun onNightThemeToggleClicked(isChecked: Boolean)
    fun onChangeInitialSectionClicked()
    fun onSelectedSectionChanged(sectionId: Int)
    fun onSelectedSectionDialogClosed()
}
