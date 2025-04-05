package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.InitialSection
import com.dezdeqness.domain.model.TimeEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun getNightThemeStatus(): Boolean

    suspend fun setNightThemeStatus(status: Boolean)

    suspend fun isLanguageDisclaimerShown(): Boolean

    suspend fun setLanguageDisclaimerShown(isShown: Boolean)

    suspend fun getSelectedInitialSection(): InitialSection

    suspend fun setSelectedInitialSection(section: InitialSection)

    suspend fun getStatusesOrder(): List<String>

    suspend fun getStatusesOrderFlow(): Flow<List<String>?>

    suspend fun setStatusesOrder(statuses: List<String>)

    suspend fun getNotificationsEnabled(): Boolean

    suspend fun setNotificationsEnabled(status: Boolean)

    suspend fun getNotificationTime(): TimeEntity

    suspend fun setNotificationTime(time: TimeEntity)
}
