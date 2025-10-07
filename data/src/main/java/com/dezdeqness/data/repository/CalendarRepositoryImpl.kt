package com.dezdeqness.data.repository

import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.datasource.CalendarRemoteDataSource
import com.dezdeqness.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
    private val settingsRepository: SettingsRepository,
) : CalendarRepository {

    override suspend fun getCalendar() = calendarRemoteDataSource.getCalendar(
        isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference),
    )

}
