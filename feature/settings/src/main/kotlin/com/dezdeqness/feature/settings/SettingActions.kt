package com.dezdeqness.feature.settings

import com.dezdeqness.feature.settings.store.core.SettingsNamespace

interface SettingActions {
    fun onBackPressed()
    fun onSettingClicked(id: String)
    fun onSwitchChanged(id: String, checked: Boolean)
    fun onDialogClosed()
    fun onDialogResult(id: String, data: SettingsNamespace.DialogState.DialogResult)
    fun invalidate()
}
