package com.dezdeqness.feature.home.data.datasource

import com.dezdeqness.contract.home.model.HomeEntity
import com.dezdeqness.data.type.OrderEnum

internal interface HomeRemoteDatasource {

    suspend fun getHomeSections(
        genreIds: List<String>,
        limit: Int,
        order: OrderEnum,
        isAdultContentEnabled: Boolean,
    ): Result<HomeEntity>
}
