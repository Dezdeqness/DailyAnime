package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.UserRateEntity

interface UserRatesRemoteDataSource {

    fun getUserRates(
        userId: Long,
        status: String,
        page: Int,
        token: String,
    ): Result<List<UserRateEntity>>

    fun updateUserRate(
        rateId: Long,
        volumes: Long,
        score: Float,
        status: String,
        rewatches: Long,
        episodes: Long,
        chapters: Long,
        comment: String,
        token: String,
    ): Result<UserRateEntity>

}
