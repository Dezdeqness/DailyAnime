package com.dezdeqness.feature.home.data

import com.dezdeqness.contract.home.repository.HomeRepository
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.type.OrderEnum
import javax.inject.Inject

internal class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDatasource: HomeRemoteDatasource,
    private val settingsRepository: SettingsRepository,
) : HomeRepository {

    override suspend fun getHomeSections(genreIds: List<String>) = homeRemoteDatasource.getHomeSections(
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
