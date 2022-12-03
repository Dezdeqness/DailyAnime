package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.UserRateEntity
import kotlinx.coroutines.flow.Flow

interface UserRatesRepository {

    fun getUserRates(status: String, page: Int): Flow<Result<List<UserRateEntity>>>

    fun getLocalUserRate(rateId: Long): UserRateEntity?

    fun updateUserRate(rateId: Long, status: String, episodes: Long, score: Float): Result<Boolean>

}
