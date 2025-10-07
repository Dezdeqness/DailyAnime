package com.dezdeqness.data.repository

import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.datasource.HomeRemoteDatasource
import com.dezdeqness.data.type.OrderEnum
import com.dezdeqness.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDatasource: HomeRemoteDatasource,
    private val settingsRepository: SettingsRepository,
) : HomeRepository {

    override suspend fun getHomeSections(genreIds: List<String>) =
        homeRemoteDatasource.getHomeSections(
            genreIds = genreIds,
            limit = SECTION_ITEM_LIMIT,
            order = SECTION_ITEM_ORDER,
            isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference),
        )

    private companion object {
        private const val SECTION_ITEM_LIMIT = 10
        private val SECTION_ITEM_ORDER = OrderEnum.popularity
    }

}
