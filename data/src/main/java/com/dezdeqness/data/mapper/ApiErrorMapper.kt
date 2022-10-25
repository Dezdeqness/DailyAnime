package com.dezdeqness.data.mapper

import com.dezdeqness.data.core.ApiException
import com.dezdeqness.domain.model.ErrorEntity
import com.dezdeqness.domain.mapper.ErrorMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiErrorMapper @Inject constructor() : ErrorMapper {

    override fun map(exception: Throwable) =
        when (exception) {
            is ApiException -> {
                ErrorEntity.UnknownErrorEntity("Code: ${exception.code}, message=${exception.message}")
            }
            else -> {
                ErrorEntity.UnknownErrorEntity("Message: ${exception.message}, stack: ${exception.stackTraceToString()}")
            }
        }

}
