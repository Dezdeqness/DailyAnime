package com.dezdeqness.data.datasource

import com.dezdeqness.contract.anime.model.UserRateEntity

interface UserRatesRemoteDataSource {

    fun getUserRates(
        userId: Long,
        status: String,
        page: Int,
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
    ): Result<UserRateEntity>

    fun createUserRate(
        userId: Long,
        targetId: String,
        targetType: String,
        volumes: Long,
        score: Float,
        status: String,
        rewatches: Long,
        episodes: Long,
        chapters: Long,
        comment: String,
    ): Result<UserRateEntity>

    fun incrementUserRate(rateId: Long): Result<UserRateEntity>

    fun deleteUserRateByRateId(rateId: Long): Result<Boolean>

}
