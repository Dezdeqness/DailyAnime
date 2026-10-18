package com.dezdeqness.di.source.unsupported

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import javax.inject.Inject

class UnsupportedUserRatesRepository @Inject constructor() : UserRatesRepository {

    override suspend fun getUserRates(status: String, page: Int, limit: Int): Result<List<UserRateEntity>> =
        unsupported()

    override suspend fun searchUserRates(
        search: String,
        statuses: String,
        page: Int,
        limit: Int,
    ): Result<List<UserRateEntity>> = unsupported()

    override fun getLocalUserRate(rateId: Long): UserRateEntity? = null

    override fun updateUserRate(
        rateId: Long,
        status: String,
        episodes: Long,
        score: Float,
        comment: String,
    ): Result<UserRateEntity> = unsupported()

    override fun createUserRate(
        targetId: String,
        status: String,
        episodes: Long,
        score: Float,
        comment: String,
    ): Result<UserRateEntity> = unsupported()

    override fun incrementUserRate(rateId: Long): Result<UserRateEntity> = unsupported()

    override fun updateLocalUserRate(userRateEntity: UserRateEntity) = Unit
}
