package com.dezdeqness.data.core

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

}

fun <T> Response<T>.createApiException() =
    ApiException(code(), errorBody()?.string() ?: "No mappable error")