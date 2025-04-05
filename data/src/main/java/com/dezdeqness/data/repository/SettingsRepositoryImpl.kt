package com.dezdeqness.data.repository

import com.dezdeqness.data.provider.PermissionCheckProvider
import com.dezdeqness.data.provider.SettingsProvider
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.domain.model.InitialSection
import com.dezdeqness.domain.model.TimeEntity
import com.dezdeqness.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsProvider: SettingsProvider,
    private val statusesProvider: StatusesProvider,
    private val permissionCheckProvider: PermissionCheckProvider,
) : SettingsRepository {

    override suspend fun getNightThemeStatus() = settingsProvider.getNightThemeStatus()

    override suspend fun setNightThemeStatus(status: Boolean) {
        settingsProvider.setNightThemeStatus(status = status)
    }

    override suspend fun isLanguageDisclaimerShown() = settingsProvider.isLanguageDisclaimerShown()

    override suspend fun setLanguageDisclaimerShown(isShown: Boolean) {
        settingsProvider.setLanguageDisclaimerShown(isShown = isShown)
    }

    override suspend fun getSelectedInitialSection() = settingsProvider.getSelectedInitialSection()

    override suspend fun setSelectedInitialSection(section: InitialSection) {
        settingsProvider.setSelectedInitialSection(section = section)
    }

    override suspend fun getStatusesOrder() =
        settingsProvider.getStatusesOrder()
            ?: statusesProvider.getStatuses().map { it.groupedId }

    override suspend fun getStatusesOrderFlow() =
        settingsProvider.getStatusesOrderFlow()

    override suspend fun setStatusesOrder(statuses: List<String>) {
        settingsProvider.setStatusesOrder(statuses)
    }

    override suspend fun getNotificationsEnabled() =
        permissionCheckProvider.isNotificationPermissionGranted() && settingsProvider.getNotificationsEnabled()

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        settingsProvider.setNotificationsEnabled(enabled = enabled)
    }

    override suspend fun getNotificationTime() = settingsProvider.getNotificationTime()

    override suspend fun setNotificationTime(time: TimeEntity) {
        settingsProvider.setNotificationTime(time = time)
    }

}
