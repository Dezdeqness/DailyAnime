package com.dezdeqness.data.core

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.dezdeqness.domain.mapper.ErrorMapper
import retrofit2.Response
import javax.inject.Inject

abstract class BaseDataSource {

    @Inject
    lateinit var errorMapper: ErrorMapper

    fun <T> tryWithCatch(block: () -> Result<T>) = try {
        block()
    } catch (exception: Throwable) {
        Result.failure(errorMapper.map(exception))
    }

    suspend fun <T> tryWithCatchSuspend(block: suspend () -> Result<T>) = try {
        block()
    } catch (exception: Throwable) {
        Result.failure(errorMapper.map(exception))
    }


}

fun <T> Response<T>.createApiException() =
    ApiException(code(), errorBody()?.string() ?: "No mappable error")

fun <T : Operation.Data> ApolloResponse<T>.createGraphqlException() =
    GraphqlException(
        operation.name(),
        errors?.toString()
            ?: exception?.toString()
            ?: "No mappable error",
    )
