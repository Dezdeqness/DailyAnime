package com.dezdeqness.domain.mapper

import com.dezdeqness.domain.model.ErrorEntity

interface ErrorMapper {

    fun map(exception: Throwable): ErrorEntity

}
