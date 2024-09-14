package com.dezdeqness.domain.repository

interface SettingsRepository {

    suspend fun getNightThemeStatus(): Boolean

    suspend fun setNightThemeStatus(status: Boolean)

    suspend fun isLanguageDisclaimerShown(): Boolean

    suspend fun setLanguageDisclaimerShown(isShown: Boolean)
}
