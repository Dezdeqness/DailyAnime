package com.dezdeqness.feature.calendar.data

import com.dezdeqness.contract.calendar.repository.CalendarRepository
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import javax.inject.Inject

internal class CalendarRepositoryImpl @Inject constructor(
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
    private val settingsRepository: SettingsRepository,
) : CalendarRepository {

    override suspend fun getCalendar() = calendarRemoteDataSource.getCalendar(
        isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference),
    )
}
