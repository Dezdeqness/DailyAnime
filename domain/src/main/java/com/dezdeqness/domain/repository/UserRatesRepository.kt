package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.UserRateEntity
import kotlinx.coroutines.flow.Flow

interface UserRatesRepository {

    fun getUserRates(status: String, page: Int = 1, onlyRemote: Boolean = false): Flow<Result<List<UserRateEntity>>>

    fun getLocalUserRate(rateId: Long): UserRateEntity?

    fun updateUserRate(rateId: Long, status: String, episodes: Long, score: Float, comment: String): Result<UserRateEntity>

    fun createUserRate(targetId: String, status: String, episodes: Long, score: Float, comment: String): Result<UserRateEntity>

    fun incrementUserRate(rateId: Long): Result<UserRateEntity>

    fun updateLocalUserRate(userRateEntity: UserRateEntity)

}
