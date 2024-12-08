package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.HomeRemoteDatasource
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.data.type.OrderEnum
import com.dezdeqness.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDatasource: HomeRemoteDatasource,
    private val homeGenresProvider: HomeGenresProvider,
) : HomeRepository {

    override suspend fun getHomeSections() =
        homeRemoteDatasource.getHomeSections(
            genres = homeGenresProvider.getHomeSectionGenresIds(),
            limit = SECTION_ITEM_LIMIT,
            order = SECTION_ITEM_ORDER)

    private companion object {
        private const val SECTION_ITEM_LIMIT = 5
        private val SECTION_ITEM_ORDER = OrderEnum.popularity
    }

}
