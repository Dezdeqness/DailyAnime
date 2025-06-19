package com.dezdeqness.data.core.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class ConfigSettingsProvider(
    private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config_settings")

    fun isOverrideRemoteEnabled() =
        runBlocking {
            context
                .dataStore
                .data
                .map { preferences -> preferences[stringPreferencesKey(CONFIG_SETTING_OVERRIDE_REMOTE)] }
                .first()
        }

    fun setOverrideRemoteEnabled(value: String) {
        runBlocking {
            context.dataStore.edit { settings ->
                settings[stringPreferencesKey(CONFIG_SETTING_OVERRIDE_REMOTE)] = value
            }
        }
    }

    companion object {
        private const val CONFIG_SETTING_OVERRIDE_REMOTE = "config_settings_override_remote"
    }
}
