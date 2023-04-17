package com.dezdeqness.data.repository

import com.dezdeqness.data.provider.SettingsProvider
import com.dezdeqness.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsProvider: SettingsProvider,
) : SettingsRepository {

    override suspend fun getNightThemeStatus() = settingsProvider.getNightThemeStatus()

    override suspend fun setNightThemeStatus(status: Boolean) {
        settingsProvider.setNightThemeStatus(status = status)
    }

}
