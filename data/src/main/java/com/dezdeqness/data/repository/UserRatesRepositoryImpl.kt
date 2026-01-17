package com.dezdeqness.data.repository

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.data.datasource.UserRatesRemoteDataSource
import com.dezdeqness.data.datasource.db.UserRatesLocalDataSource
import com.dezdeqness.data.exception.UserLocalNotFound
import com.dezdeqness.domain.model.UserRateOrderEntity
import com.dezdeqness.domain.repository.UserRatesRepository
import javax.inject.Inject

class UserRatesRepositoryImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val userRatesRemoteDataSource: UserRatesRemoteDataSource,
    private val userRatesLocalDataSource: UserRatesLocalDataSource,
    private val settingsRepository: SettingsRepository,
) : UserRatesRepository {

    override suspend fun getUserRates(
        status: String,
        page: Int,
        limit: Int,
        order: UserRateOrderEntity
    ): Result<List<UserRateEntity>> {
        val profile = userRepository.getProfileLocal() ?: return Result.failure(UserLocalNotFound())

        val isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference)

        return userRatesRemoteDataSource
            .getUserRates(
                userId = profile.id,
                page = page,
                status = status,
                limit = limit,
                isAdultContentEnabled = isAdultContentEnabled,
                order = order,
            )
            .onSuccess { list ->
                userRatesLocalDataSource.saveUserRates(list)
            }
    }

    override fun getLocalUserRate(rateId: Long) =
        userRatesLocalDataSource.getUserRate(rateId = rateId)

    override fun updateUserRate(
        rateId: Long,
        status: String,
        episodes: Long,
        score: Float,
        comment: String,
    ): Result<UserRateEntity> {
        val localUserRate = userRatesLocalDataSource.getUserRate(rateId)
            ?: return Result.failure(UserLocalNotFound())

        val result = userRatesRemoteDataSource.updateUserRate(
            rateId = rateId,
            score = score,
            status = status,
            episodes = episodes,
            volumes = localUserRate.volumes,
            rewatches = localUserRate.rewatches,
            chapters = localUserRate.chapters,
            comment = comment,
        )
        return result
            .onSuccess { entity ->
                userRatesLocalDataSource.updateUserRate(entity)
            }
    }

    override fun incrementUserRate(rateId: Long): Result<UserRateEntity> {
        val result = userRatesRemoteDataSource.incrementUserRate(
            rateId = rateId,
        )
        return result
            .onSuccess { entity ->
                userRatesLocalDataSource.updateUserRate(entity)
            }
    }

    override fun createUserRate(
        targetId: String,
        status: String,
        episodes: Long,
        score: Float,
        comment: String,
    ): Result<UserRateEntity> {
        val profile = userRepository.getProfileLocal()
            ?: return Result.failure(UserLocalNotFound())

        val result = userRatesRemoteDataSource.createUserRate(
            userId = profile.id,
            targetId = targetId,
            targetType = "Anime",
            score = score,
            status = status,
            episodes = episodes,
            volumes = 0,
            rewatches = 0,
            chapters = 0,
            comment = comment,
        )
        return result
            .onSuccess { entity ->
                userRatesLocalDataSource.insertUserRate(entity)
            }
    }

    override fun updateLocalUserRate(userRateEntity: UserRateEntity) {
        userRatesLocalDataSource.updateUserRate(userRateEntity)
    }
}
