package com.dezdeqness.contract.settings.repository

import com.dezdeqness.contract.settings.core.SettingsPreference
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun <T> getPreference(key: SettingsPreference<T>): T
    suspend fun <T> setPreference(key: SettingsPreference<T>, value: T)
    fun <T> observePreference(key: SettingsPreference<T>): Flow<T>
}
