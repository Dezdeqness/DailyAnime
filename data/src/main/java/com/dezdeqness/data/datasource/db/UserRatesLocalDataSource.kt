package com.dezdeqness.data.datasource.db

import com.dezdeqness.domain.model.UserRateEntity


interface UserRatesLocalDataSource {

    fun getUserRates(): List<UserRateEntity>

    fun getUserRatesByStatus(status: String): List<UserRateEntity>

    fun getUserRate(rateId: Long): UserRateEntity?

    fun saveUserRates(list: List<UserRateEntity>)

    fun deleteUserRates()

    fun deleteUserRatesByStatus(status: String)

    fun deleteUserRatesByRateId(rateId: Long)

    fun updateUserRate(userRateEntity: UserRateEntity)

}
