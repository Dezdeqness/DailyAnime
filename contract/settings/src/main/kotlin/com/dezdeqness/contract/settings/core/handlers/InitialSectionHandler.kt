package com.dezdeqness.contract.settings.core.handlers

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.dezdeqness.contract.settings.core.PreferenceHandler
import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.models.InitialSection

object InitialSectionHandler : PreferenceHandler<InitialSection> {
    override fun read(prefs: Preferences, key: SettingsPreference<InitialSection>): InitialSection? {
        val id = prefs[intPreferencesKey(key.name)]
        return id?.let { InitialSection.fromId(it) }
    }

    override fun write(
        prefs: MutablePreferences,
        key: SettingsPreference<InitialSection>,
        value: InitialSection
    ) {
        prefs[intPreferencesKey(key.name)] = value.id
    }
}
