package com.dezdeqness.domain.core.mapper

import com.dezdeqness.domain.core.model.ErrorEntity

interface ErrorMapper {

    fun map(exception: Throwable): ErrorEntity
}
