package com.dezdeqness.data.datasource.db

import com.dezdeqness.data.datasource.db.dao.UserRatesDao
import com.dezdeqness.data.mapper.UserRatesMapper
import com.dezdeqness.contract.anime.model.UserRateEntity
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
        userRatesDao.insertUserRate(userRatesMapper.toDatabase(userRateEntity))
    }
}
