package com.dezdeqness.domain.repository

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.domain.model.UserRateOrderEntity

interface UserRatesRepository {

    suspend fun getUserRates(
        status: String,
        page: Int = 1,
        limit: Int = 50,
        order: UserRateOrderEntity = UserRateOrderEntity.UPDATED_AT_DESC
    ): Result<List<UserRateEntity>>

    fun getLocalUserRate(rateId: Long): UserRateEntity?

    fun updateUserRate(rateId: Long, status: String, episodes: Long, score: Float, comment: String): Result<UserRateEntity>

    fun createUserRate(targetId: String, status: String, episodes: Long, score: Float, comment: String): Result<UserRateEntity>

    fun incrementUserRate(rateId: Long): Result<UserRateEntity>

    fun updateLocalUserRate(userRateEntity: UserRateEntity)

}
