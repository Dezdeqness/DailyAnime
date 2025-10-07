package com.dezdeqness.data.datasource

import com.dezdeqness.data.type.OrderEnum
import com.dezdeqness.domain.model.HomeEntity

interface HomeRemoteDatasource {

    suspend fun getHomeSections(
        genreIds: List<String>,
        limit: Int,
        order: OrderEnum,
        isAdultContentEnabled: Boolean,
    ): Result<HomeEntity>

}
