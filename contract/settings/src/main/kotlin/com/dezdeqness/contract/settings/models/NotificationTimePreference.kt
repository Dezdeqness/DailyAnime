package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.NotificationTimeHandler

data object NotificationTimePreference : SettingsPreference<TimeEntity> {
    override val name = "notificationTime"
    override val default = TimeEntity(hours = 19, minutes = 0)
    override val handler = NotificationTimeHandler
}

data class TimeEntity(
    val hours: Int,
    val minutes: Int,
)
