package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.BooleanHandler

data object NotificationEnabledPreference : SettingsPreference<Boolean> {
    override val name = "notificationsEnabled"
    override val default = false
    override val handler = BooleanHandler
}
