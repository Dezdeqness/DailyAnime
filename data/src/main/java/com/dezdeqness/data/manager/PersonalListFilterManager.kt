package com.dezdeqness.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonalListFilterManager @Inject constructor(
    private val context: Context,
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    suspend fun setOrder(isAscending: Boolean) {
        context.dataStore.edit { settings ->
            settings[ORDER] = isAscending
        }
    }

    suspend fun getOrder(defaultValue: Boolean) =
        context
            .dataStore
            .data
            .map { preferences -> preferences[ORDER] }
            .first()
            ?: defaultValue

    suspend fun setSort(sort: String) {
        context.dataStore.edit { settings ->
            settings[SORT] = sort
        }
    }

    suspend fun getSort(defaultValue: String) =
        context
            .dataStore
            .data
            .map { preferences -> preferences[SORT] }
            .first()
            ?: defaultValue

    companion object {
        private val ORDER = booleanPreferencesKey("order")
        private val SORT = stringPreferencesKey("sort")
    }

}
