package com.dezdeqness.feature.userrate.data.datasource

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.feature.userrate.data.database.UserRatesDao
import com.dezdeqness.feature.userrate.data.mapper.UserRatesDbMapper
import javax.inject.Inject

internal class UserRatesLocalDataSourceImpl @Inject constructor(
    private val userRatesDbMapper: UserRatesDbMapper,
    private val userRatesDao: UserRatesDao,
) : UserRatesLocalDataSource {

    override fun getUserRates() = userRatesDao
        .getUserRates()
        .map(userRatesDbMapper::fromDatabase)

    override fun getUserRatesByStatus(status: String) = userRatesDao
        .getUserRates(status = status)
        .map(userRatesDbMapper::fromDatabase)

    override fun getUserRate(rateId: Long) = userRatesDao
        .getUserRateByRateId(rateId)
        ?.let { userRatesDbMapper.fromDatabase(it) }

    override fun saveUserRates(list: List<UserRateEntity>) {
        val localList = list.map(userRatesDbMapper::toDatabase)
        userRatesDao.saveUserRates(localList)
    }

    override fun deleteUserRates() {
        userRatesDao.deleteUserRates()
    }

    override fun deleteUserRatesByStatus(status: String) {
        userRatesDao.deleteUserRatesByStatus(status)
    }

    override fun deleteUserRatesByRateId(rateId: Long) {
        userRatesDao.deleteUserRateByRateId(rateId)
    }

    override fun updateUserRate(userRateEntity: UserRateEntity) {
        userRatesDao.updateUserRate(
            rateId = userRateEntity.id.toInt(),
            score = userRateEntity.score.toInt(),
            status = userRateEntity.status,
            episodes = userRateEntity.episodes.toInt(),
            text = userRateEntity.text,
        )
    }

    override fun insertUserRate(userRateEntity: UserRateEntity) {
        userRatesDao.insertUserRate(userRatesDbMapper.toDatabase(userRateEntity))
    }
}
