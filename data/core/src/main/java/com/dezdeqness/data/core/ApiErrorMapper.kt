package com.dezdeqness.data.core

import com.dezdeqness.domain.core.mapper.ErrorMapper
import com.dezdeqness.domain.core.model.ErrorEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiErrorMapper @Inject constructor() : ErrorMapper {

    override fun map(exception: Throwable) = when (exception) {
        is ApiException -> {
            ErrorEntity.UnknownErrorEntity("Code: ${exception.code}, message=${exception.message}")
        }
        is GraphqlException -> {
            ErrorEntity.UnknownErrorEntity(
                "Graphql operation name: ${exception.operationName}, message=${exception.message}",
            )
        }
        else -> {
            ErrorEntity.UnknownErrorEntity(
                "Message: ${exception.message}, stack: ${exception.stackTraceToString()}",
            )
        }
    }
}
