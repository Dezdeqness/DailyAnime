package com.dezdeqness.data.datasource

import com.dezdeqness.data.UserRatesApiService
import com.dezdeqness.data.core.ApiException
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.mapper.UserRatesMapper
import com.dezdeqness.data.model.requet.UserRate
import com.dezdeqness.data.model.requet.UserRateRequestBody
import com.dezdeqness.domain.model.UserRateEntity
import javax.inject.Inject

class UserRatesRemoteDataSourceImpl @Inject constructor(
    private val apiService: UserRatesApiService,
    private val userRatesMapper: UserRatesMapper,
) : UserRatesRemoteDataSource, BaseDataSource() {

    override fun getUserRates(userId: Long, status: String, page: Int, token: String) =
        tryWithCatch {
            val response = apiService.gerUserRates(
                status = status,
                id = userId,
                page = page,
                limit = 1000,
                token = "Bearer $token",
            ).execute()

            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                Result.success(
                    responseBody.map(userRatesMapper::fromResponse)
                )
            } else {
                throw ApiException(response.code(), response.errorBody().toString())
            }
        }

    override fun updateUserRate(
        rateId: Long,
        volumes: Long,
        score: Float,
        status: String,
        rewatches: Long,
        episodes: Long,
        chapters: Long,
        comment: String,
        token: String,
    ) = tryWithCatch {
        val body = UserRateRequestBody(
            userRate = UserRate(
                chapters = chapters.toString(),
                episodes = episodes.toString(),
                rewatches = rewatches.toString(),
                score = score.toString(),
                status = status,
                text = comment,
                volumes = volumes.toString()
            )
        )
        val response = apiService.updateUserRate(
            id = rateId,
            body = body,
            token = "Bearer $token",
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(userRatesMapper.fromResponse(responseBody))
        } else {
            throw ApiException(response.code(), response.errorBody().toString())
        }
    }


}
