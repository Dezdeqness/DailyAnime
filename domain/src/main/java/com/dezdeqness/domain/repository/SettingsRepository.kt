package com.dezdeqness.domain.repository

interface SettingsRepository {

    suspend fun getNightThemeStatus(): Boolean

    suspend fun setNightThemeStatus(status: Boolean)

}
