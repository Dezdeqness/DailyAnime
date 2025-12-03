package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.anime.model.Entity

abstract class BaseListableUseCase {

    abstract suspend fun invoke(id: Long): Result<List<Entity>>

}
