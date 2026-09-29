package com.dezdeqness.contract.settings.core.handlers

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dezdeqness.contract.settings.core.PreferenceHandler
import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.source.SourceType

object SourceTypeHandler : PreferenceHandler<SourceType> {
    override fun read(prefs: Preferences, key: SettingsPreference<SourceType>): SourceType? {
        val name = prefs[stringPreferencesKey(key.name)]
        return SourceType.entries.find { it.name == name }
    }

    override fun write(prefs: MutablePreferences, key: SettingsPreference<SourceType>, value: SourceType) {
        prefs[stringPreferencesKey(key.name)] = value.name
    }
}
