package com.dezdeqness.data.core

import com.dezdeqness.domain.mapper.ErrorMapper
import javax.inject.Inject

abstract class BaseDataSource {

    @Inject
    protected lateinit var errorMapper: ErrorMapper

    fun <T> tryWithCatch(block: () -> Result<T>) = try {
        block()
    } catch (exception: Throwable) {
        Result.failure(errorMapper.map(exception))
    }

}
