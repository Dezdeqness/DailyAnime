package com.dezdeqness.data.provider

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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

    suspend fun setLanguageDisclaimerShown(isShown: Boolean) {
        context.dataStore.edit { settings ->
            settings[IS_LANGUAGE_DISCLAIMER_DIALOG_SHOWN] = isShown
        }
    }

    suspend fun isLanguageDisclaimerShown() =
        context
            .dataStore
            .data
            .map { preferences -> preferences[IS_LANGUAGE_DISCLAIMER_DIALOG_SHOWN] }
            .first()
            ?: false

    suspend fun setSelectedInitialSection(sectionId: Int) {
        context.dataStore.edit { settings ->
            settings[SELECTED_INITIAL_SECTION] = sectionId
        }
    }

    suspend fun getSelectedInitialSection() =
        context
            .dataStore
            .data
            .map { preferences -> preferences[SELECTED_INITIAL_SECTION] }
            .first()

    suspend fun setStatusesOrder(statuses: List<String>) {
        context.dataStore.edit { settings ->
            settings[STATUSES_ORDER] = statuses.joinToString(SEPARATOR)
        }
    }

    suspend fun getStatusesOrder() =
        context
            .dataStore
            .data
            .map { preferences -> preferences[STATUSES_ORDER]?.split(SEPARATOR) }
            .first()

    suspend fun getStatusesOrderFlow() =
        context
            .dataStore
            .data
            .map { preferences -> preferences[STATUSES_ORDER]?.split(SEPARATOR) }

    companion object {
        private val IS_NIGHT_THEME = booleanPreferencesKey("nightTheme")
        private val IS_LANGUAGE_DISCLAIMER_DIALOG_SHOWN = booleanPreferencesKey("languageDisclaimerShown")
        private val SELECTED_INITIAL_SECTION = intPreferencesKey("selectedInitialSection")
        private val STATUSES_ORDER = stringPreferencesKey("statusOrder")

        private const val SEPARATOR = ","
    }

}
