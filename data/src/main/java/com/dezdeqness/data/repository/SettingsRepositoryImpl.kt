package com.dezdeqness.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val context: Context,
) : SettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

    override suspend fun <T> getPreference(key: SettingsPreference<T>) =
        context
            .dataStore
            .data
            .firstOrNull()
            ?.let { key.handler.read(it, key) }
            ?: key.default

    override suspend fun <T> setPreference(key: SettingsPreference<T>, value: T) {
        context.dataStore.edit { prefs -> key.handler.write(prefs, key, value) }
    }

    override fun <T> observePreference(key: SettingsPreference<T>): Flow<T> {
        return context.dataStore.data.map { prefs ->
            key.handler.read(prefs, key) ?: key.default
        }
    }

}
