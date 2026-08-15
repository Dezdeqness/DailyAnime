package com.dezdeqness.data.datasource

import com.dezdeqness.contract.home.model.HomeEntity
import com.dezdeqness.data.type.OrderEnum

interface HomeRemoteDatasource {

    suspend fun getHomeSections(
        genreIds: List<String>,
        limit: Int,
        order: OrderEnum,
        isAdultContentEnabled: Boolean,
    ): Result<HomeEntity>
}
