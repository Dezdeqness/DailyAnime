package com.dezdeqness.data.core.config.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dezdeqness.data.core.config.BaseConfigProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// TODO: Move to core module
class DebugConfigProvider(
    private val context: Context,
) : BaseConfigProvider {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "debug_config")

    override fun getStringValue(key: String) =
        runBlocking {
            context
                .dataStore
                .data
                .map { preferences -> preferences[stringPreferencesKey(key)] }
                .first()
        }

    fun setStringValue(key: String, value: String) {
        runBlocking {
            context.dataStore.edit { settings ->
                settings[stringPreferencesKey(key)] = value
            }
        }
    }

    override fun getIntValue(key: String) =
        runBlocking {
            context
                .dataStore
                .data
                .map { preferences -> preferences[intPreferencesKey(key)] }
                .first()
        }

    fun setIntValue(key: String, value: Int) {
        runBlocking {
            context.dataStore.edit { settings ->
                settings[intPreferencesKey(key)] = value
            }
        }
    }

    override fun getDoubleValue(key: String) =
        runBlocking {
            context
                .dataStore
                .data
                .map { preferences -> preferences[doublePreferencesKey(key)] }
                .first()
        }

    fun setDoubleValue(key: String, value: Double) {
        runBlocking {
            context.dataStore.edit { settings ->
                settings[doublePreferencesKey(key)] = value
            }
        }
    }

    override fun getBooleanValue(key: String) =
        runBlocking {
            context
                .dataStore
                .data
                .map { preferences ->
                    runCatching {
                        preferences[booleanPreferencesKey(key)]
                    }
                        .getOrNull()
                }
                .first()
        }

    fun setBooleanValue(key: String, value: Boolean) {
        runBlocking {
            context.dataStore.edit { settings ->
                settings[booleanPreferencesKey(key)] = value
            }
        }
    }

}
