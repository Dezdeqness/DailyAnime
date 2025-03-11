package com.dezdeqness.data.repository

import com.dezdeqness.data.provider.SettingsProvider
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsProvider: SettingsProvider,
    private val statusesProvider: StatusesProvider,
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

    override suspend fun setSelectedInitialSection(sectionId: Int) {
        settingsProvider.setSelectedInitialSection(sectionId = sectionId)
    }

    override suspend fun getStatusesOrder() =
        settingsProvider.getStatusesOrder()
            ?: statusesProvider.getStatuses().map { it.groupedId }.toSet()

    override suspend fun setStatusesOrder(statuses: Set<String>) {
        settingsProvider.setStatusesOrder(statuses)
    }

}
