package com.dezdeqness.contract.settings.core.handlers

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.dezdeqness.contract.settings.core.PreferenceHandler
import com.dezdeqness.contract.settings.core.SettingsPreference

object IntHandler : PreferenceHandler<Int> {
    override fun read(prefs: Preferences, key: SettingsPreference<Int>): Int? =
        prefs[intPreferencesKey(key.name)]

    override fun write(prefs: MutablePreferences, key: SettingsPreference<Int>, value: Int) {
        prefs[intPreferencesKey(key.name)] = value
    }
}
