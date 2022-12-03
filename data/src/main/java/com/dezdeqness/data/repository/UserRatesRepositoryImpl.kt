package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.UserRatesRemoteDataSource
import com.dezdeqness.data.datasource.db.UserRatesLocalDataSource
import com.dezdeqness.data.manager.TokenManager
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
    private val tokenManager: TokenManager,
) : UserRatesRepository {

    override fun getUserRates(status: String, page: Int): Flow<Result<List<UserRateEntity>>> =
        flow {
            val profile = accountRepository.getProfileLocal()
            if (profile == null) {
                emit(Result.failure(Exception()))
                return@flow
            }
            val token = tokenManager.getTokenData()

            val localList = userRatesLocalDataSource.getUserRatesByStatus(status = status)

            if (localList.isNotEmpty()) {
                emit(Result.success(localList))
            }

            emit(
                userRatesRemoteDataSource
                    .getUserRates(
                        token = token.accessToken,
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
        val tokenData = tokenManager.getTokenData()

        val localUserRate = userRatesLocalDataSource.getUserRate(rateId)
            ?: return Result.failure(Exception())

        val result = userRatesRemoteDataSource.updateUserRate(
            rateId = rateId,
            token = tokenData.accessToken,
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
}
