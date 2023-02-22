package com.dezdeqness.data.datasource.db

import com.dezdeqness.data.datasource.db.dao.UserRatesDao
import com.dezdeqness.data.mapper.UserRatesMapper
import com.dezdeqness.domain.model.UserRateEntity
import javax.inject.Inject

class UserRatesLocalDataSourceImpl @Inject constructor(
    private val userRatesMapper: UserRatesMapper,
    private val userRatesDao: UserRatesDao,
) : UserRatesLocalDataSource {

    override fun getUserRates() =
        userRatesDao
            .getUserRates()
            .map(userRatesMapper::fromDatabase)

    override fun getUserRatesByStatus(status: String) =
        userRatesDao
            .getUserRates(status = status)
            .map(userRatesMapper::fromDatabase)

    override fun getUserRate(rateId: Long) =
        userRatesDao
            .getUserRateByRateId(rateId)
            ?.let { userRatesMapper.fromDatabase(it) }

    override fun saveUserRates(list: List<UserRateEntity>) {
        val localList = list.map(userRatesMapper::toDatabase)
        userRatesDao.saveUserRates(localList)
    }

    override fun deleteUserRates() {
        userRatesDao.deleteUserRates()
    }

    override fun deleteUserRatesByStatus(status: String) {
        userRatesDao.deleteUserRatesByStatus(status)
    }
}
