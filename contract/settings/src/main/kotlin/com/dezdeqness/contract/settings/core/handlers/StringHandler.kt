package com.dezdeqness.contract.settings.core.handlers

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dezdeqness.contract.settings.core.PreferenceHandler
import com.dezdeqness.contract.settings.core.SettingsPreference

object StringHandler : PreferenceHandler<String> {
    override fun read(prefs: Preferences, key: SettingsPreference<String>): String? =
        prefs[stringPreferencesKey(key.name)]

    override fun write(prefs: MutablePreferences, key: SettingsPreference<String>, value: String) {
        prefs[stringPreferencesKey(key.name)] = value
    }
}
