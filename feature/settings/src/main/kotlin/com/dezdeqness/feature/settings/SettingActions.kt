package com.dezdeqness.feature.settings

interface SettingActions {
    fun onBackPressed()
    fun onSettingClicked(id: String)
    fun onSwitchChanged(id: String, checked: Boolean)
}
