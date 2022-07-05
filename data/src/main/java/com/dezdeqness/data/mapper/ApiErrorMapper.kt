package com.dezdeqness.data.mapper

import com.dezdeqness.data.core.ApiException
import com.dezdeqness.domain.model.ErrorEntity
import com.dezdeqness.domain.mapper.ErrorMapper
import javax.inject.Inject

class ApiErrorMapper @Inject constructor() : ErrorMapper {

    override fun map(exception: Throwable) =
        when(exception) {
            is ApiException -> {
                ErrorEntity.UnknownErrorEntity("")
            }
            else -> {
                ErrorEntity.UnknownErrorEntity("")
            }
        }

}
