package com.dezdeqness.feature.userrate.data

import com.dezdeqness.contract.anime.model.UserRateEntity

internal interface UserRatesLocalDataSource {

    fun getUserRates(): List<UserRateEntity>

    fun getUserRatesByStatus(status: String): List<UserRateEntity>

    fun getUserRate(rateId: Long): UserRateEntity?

    fun saveUserRates(list: List<UserRateEntity>)

    fun deleteUserRates()

    fun deleteUserRatesByStatus(status: String)

    fun deleteUserRatesByRateId(rateId: Long)

    fun updateUserRate(userRateEntity: UserRateEntity)

    fun insertUserRate(userRateEntity: UserRateEntity)
}
