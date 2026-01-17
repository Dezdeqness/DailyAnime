package com.dezdeqness.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.dezdeqness.data.UserRatesApiService
import com.dezdeqness.data.UserRatesQuery
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.core.createGraphqlException
import com.dezdeqness.data.mapper.UserRatesMapper
import com.dezdeqness.data.model.requet.PostUserRate
import com.dezdeqness.data.model.requet.PostUserRateRequestBody
import com.dezdeqness.data.model.requet.UpdateUserRate
import com.dezdeqness.data.model.requet.UpdateUserRateRequestBody
import com.dezdeqness.data.type.SortOrderEnum
import com.dezdeqness.data.type.UserRateOrderFieldEnum
import com.dezdeqness.data.type.UserRateOrderInputType
import com.dezdeqness.data.type.UserRateStatusEnum
import com.dezdeqness.domain.model.UserRateOrderEntity
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named

class UserRatesRemoteDataSourceImpl @Inject constructor(
    private val apiService: Lazy<UserRatesApiService>,
    @Named("shikimori_graphql_client") private val apolloClient: ApolloClient,
    private val userRatesMapper: UserRatesMapper,
) : UserRatesRemoteDataSource, BaseDataSource() {

    override suspend fun getUserRates(
        userId: Long,
        status: String,
        page: Int,
        limit: Int,
        isAdultContentEnabled: Boolean,
        order: UserRateOrderEntity,
    ) =
        tryWithCatchSuspend {
            val statusEnum = UserRateStatusEnum.safeValueOf(status)
            val response = apolloClient.query(
                UserRatesQuery(
                    page = page,
                    limit = limit,
                    status = statusEnum,
                    order = Optional.present(
                        UserRateOrderInputType(
                            field = UserRateOrderFieldEnum.updated_at,
                            order = SortOrderEnum.desc,
                        ),
                    ),
                )
            ).execute()

            val data = response.data

            if (data != null) {
                val userRates = data.userRates.mapNotNull { userRate ->
                    userRatesMapper.fromResponseGraphql(userRate)
                }
                Result.success(userRates)
            } else {
                throw response.createGraphqlException()
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
