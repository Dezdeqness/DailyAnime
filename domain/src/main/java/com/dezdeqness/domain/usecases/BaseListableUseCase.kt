package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.model.Entity

abstract class BaseListableUseCase {

    abstract fun invoke(id: Long): Result<List<Entity>>

}
