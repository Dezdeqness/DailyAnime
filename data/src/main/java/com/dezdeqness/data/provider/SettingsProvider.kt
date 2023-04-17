package com.dezdeqness.data.provider

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class SettingsProvider @Inject constructor(
    private val context: Context,
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

    suspend fun setNightThemeStatus(status: Boolean) {
        context.dataStore.edit { settings ->
            settings[IS_NIGHT_THEME] = status
        }
    }

    suspend fun getNightThemeStatus() =
        context
            .dataStore
            .data
            .map { preferences -> preferences[IS_NIGHT_THEME] }
            .first()
            ?: false

    companion object {
        private val IS_NIGHT_THEME = booleanPreferencesKey("nightTheme")
    }

}
