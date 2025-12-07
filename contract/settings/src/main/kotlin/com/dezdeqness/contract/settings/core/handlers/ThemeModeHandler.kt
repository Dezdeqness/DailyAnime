package com.dezdeqness.contract.settings.core.handlers

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.dezdeqness.contract.settings.core.PreferenceHandler
import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.models.ThemeMode

object ThemeModeHandler : PreferenceHandler<ThemeMode> {
    override fun read(prefs: Preferences, key: SettingsPreference<ThemeMode>): ThemeMode? {
        val intKey = intPreferencesKey(key.name)
        val id = prefs[intKey]
        return if (id != null) {
            ThemeMode.fromId(id)
        } else {
            ThemeMode.SYSTEM
        }
    }

    override fun write(
        prefs: MutablePreferences,
        key: SettingsPreference<ThemeMode>,
        value: ThemeMode,
    ) {
        prefs[intPreferencesKey(key.name)] = value.id
    }
}
