package com.dezdeqness.data.datasource

import com.dezdeqness.data.UserRatesApiService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.UserRatesMapper
import com.dezdeqness.data.model.requet.PostUserRate
import com.dezdeqness.data.model.requet.PostUserRateRequestBody
import com.dezdeqness.data.model.requet.UpdateUserRate
import com.dezdeqness.data.model.requet.UpdateUserRateRequestBody
import dagger.Lazy
import javax.inject.Inject

class UserRatesRemoteDataSourceImpl @Inject constructor(
    private val apiService: Lazy<UserRatesApiService>,
    private val userRatesMapper: UserRatesMapper,
) : UserRatesRemoteDataSource, BaseDataSource() {

    override fun getUserRates(
        userId: Long,
        status: String,
        page: Int,
        isAdultContentEnabled: Boolean,
    ) =
        tryWithCatch {
            val response = apiService.get().getUserRates(
                status = status,
                id = userId,
                page = page,
                limit = 1000,
                isAdultContentEnabled = isAdultContentEnabled,
            ).execute()

            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                Result.success(
                    responseBody.map(userRatesMapper::fromResponse)
                )
            } else {
                throw response.createApiException()
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
    ) = tryWithCatch {
        val body = UpdateUserRateRequestBody(
            userRate = UpdateUserRate(
                chapters = chapters.toString(),
                episodes = episodes.toString(),
                rewatches = rewatches.toString(),
                score = score.toString(),
                status = status,
                text = comment,
                volumes = volumes.toString()
            )
        )
        val response = apiService.get().updateUserRate(
            id = rateId,
            body = body,
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(userRatesMapper.fromResponse(responseBody))
        } else {
            throw response.createApiException()
        }
    }

    override fun createUserRate(
        userId: Long,
        targetId: String,
        targetType: String,
        volumes: Long,
        score: Float,
        status: String,
        rewatches: Long,
        episodes: Long,
        chapters: Long,
        comment: String,
    ) = tryWithCatch {
        val body = PostUserRateRequestBody(
            userRate = PostUserRate(
                userId = userId,
                targetId = targetId,
                targetType = targetType,
                chapters = chapters.toString(),
                episodes = episodes.toString(),
                rewatches = rewatches.toString(),
                score = score.toString(),
                status = status,
                text = comment,
                volumes = volumes.toString(),
            )
        )
        val response = apiService.get().createUserRate(body = body).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(userRatesMapper.fromResponse(responseBody))
        } else {
            throw response.createApiException()
        }
    }

    override fun incrementUserRate(rateId: Long) = tryWithCatch {
        val response = apiService.get().incrementUserRate(id = rateId).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(userRatesMapper.fromResponse(responseBody))
        } else {
            throw response.createApiException()
        }
    }

    override fun deleteUserRateByRateId(rateId: Long) = tryWithCatch {
        val response = apiService.get().deleteUserRate(rateId).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(true)
        } else {
            throw response.createApiException()
        }
    }

}
