package com.dezdeqness.contract.settings.core.handlers

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dezdeqness.contract.settings.core.PreferenceHandler
import com.dezdeqness.contract.settings.core.SettingsPreference

object StringListHandler : PreferenceHandler<List<String>> {
    private const val SEPARATOR = ","

    override fun read(prefs: Preferences, key: SettingsPreference<List<String>>): List<String>? {
        return prefs[stringPreferencesKey(key.name)]?.split(SEPARATOR)
    }

    override fun write(
        prefs: MutablePreferences,
        key: SettingsPreference<List<String>>,
        value: List<String>
    ) {
        prefs[stringPreferencesKey(key.name)] = value.joinToString(SEPARATOR)
    }
}
