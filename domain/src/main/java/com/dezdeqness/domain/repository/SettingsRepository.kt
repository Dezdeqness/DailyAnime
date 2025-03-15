package com.dezdeqness.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun getNightThemeStatus(): Boolean

    suspend fun setNightThemeStatus(status: Boolean)

    suspend fun isLanguageDisclaimerShown(): Boolean

    suspend fun setLanguageDisclaimerShown(isShown: Boolean)

    suspend fun getSelectedInitialSection(): Int?

    suspend fun setSelectedInitialSection(sectionId: Int)

    suspend fun getStatusesOrder(): List<String>

    suspend fun getStatusesOrderFlow(): Flow<List<String>?>

    suspend fun setStatusesOrder(statuses: List<String>)
}
