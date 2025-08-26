package com.dezdeqness.data.provider

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.text.split

@Singleton
class HistorySearchListProvider @Inject constructor(
    private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

    suspend fun setSearchHistory(statuses: List<String>) {
        context
            .dataStore
            .edit { settings ->
                settings[HISTORY] = statuses.joinToString(SEPARATOR)
            }
    }

    fun getSearchHistoryFlow() = context
        .dataStore
        .data
        .map { preferences ->
            val historyString = preferences[HISTORY]

            if (historyString.isNullOrBlank()) {
                return@map emptyList()
            } else {
                historyString.split(SEPARATOR)
            }
        }

    suspend fun getSearchHistory() = getSearchHistoryFlow().first()

    companion object {
        private val HISTORY = stringPreferencesKey("history")

        private const val SEPARATOR = ","
    }
}
