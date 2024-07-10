package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.UserRatesRemoteDataSource
import com.dezdeqness.data.datasource.db.UserRatesLocalDataSource
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class UserRatesRepositoryImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userRatesRemoteDataSource: UserRatesRemoteDataSource,
    private val userRatesLocalDataSource: UserRatesLocalDataSource,
) : UserRatesRepository {

    override fun getUserRates(status: String, page: Int, onlyRemote: Boolean): Flow<Result<List<UserRateEntity>>> =
        flow {
            val profile = accountRepository.getProfileLocal()
            if (profile == null) {
                emit(Result.failure(Exception("Local profile failure")))
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
        score: Float
    ): Result<Boolean> {
        val localUserRate = userRatesLocalDataSource.getUserRate(rateId)
            ?: return Result.failure(Exception("Local user rate failure"))

        val result = userRatesRemoteDataSource.updateUserRate(
            rateId = rateId,
            score = score,
            status = status,
            episodes = episodes,
            volumes = localUserRate.volumes,
            rewatches = localUserRate.rewatches,
            chapters = localUserRate.chapters,
            comment = localUserRate.text,
        )
        return result.map { true }
    }

    override fun createUserRate(targetId: String, status: String, episodes: Long, score: Float): Result<Boolean> {
        val profile = accountRepository.getProfileLocal() ?: return Result.failure(Exception("Profile local failure"))

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
            comment = "",
        )
        return result.map { true }
    }

    override fun updateLocalUserRate(userRateEntity: UserRateEntity) {
        userRatesLocalDataSource.updateUserRate(userRateEntity)
    }
}
