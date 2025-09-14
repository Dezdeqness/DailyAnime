package com.dezdeqness.contract.settings.core.handlers

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.dezdeqness.contract.settings.core.PreferenceHandler
import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.models.TimeEntity

object NotificationTimeHandler : PreferenceHandler<TimeEntity> {
    private fun hoursKey() = intPreferencesKey("notificationsHours")
    private fun minutesKey() = intPreferencesKey("notificationsMinutes")

    override fun read(prefs: Preferences, key: SettingsPreference<TimeEntity>): TimeEntity? {
        val hours = prefs[hoursKey()] ?: return null
        val minutes = prefs[minutesKey()] ?: return null
        return TimeEntity(hours = hours, minutes = minutes)
    }

    override fun write(
        prefs: MutablePreferences,
        key: SettingsPreference<TimeEntity>,
        value: TimeEntity
    ) {
        prefs[hoursKey()] = value.hours
        prefs[minutesKey()] = value.minutes
    }
}
