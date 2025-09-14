package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.UserRatesRemoteDataSource
import com.dezdeqness.data.datasource.db.UserRatesLocalDataSource
import com.dezdeqness.data.exception.UserLocalNotFound
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRatesRepositoryImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val userRatesRemoteDataSource: UserRatesRemoteDataSource,
    private val userRatesLocalDataSource: UserRatesLocalDataSource,
) : UserRatesRepository {

    override fun getUserRates(status: String, page: Int, onlyRemote: Boolean): Flow<Result<List<UserRateEntity>>> =
        flow {
            val profile = userRepository.getProfileLocal()
            if (profile == null) {
                emit(Result.failure(UserLocalNotFound()))
                return@flow
            }

            if (onlyRemote.not()) {
                val localList = userRatesLocalDataSource.getUserRatesByStatus(status = status)

                if (localList.isNotEmpty()) {
                    emit(Result.success(localList))
                }
            }

            emit(
                userRatesRemoteDataSource
                    .getUserRates(
                        userId = profile.id,
                        page = page,
                        status = status,
                    )
                    .onSuccess { list ->
                        userRatesLocalDataSource.deleteUserRatesByStatus(status)
                        userRatesLocalDataSource.saveUserRates(list)
                    }
            )

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
