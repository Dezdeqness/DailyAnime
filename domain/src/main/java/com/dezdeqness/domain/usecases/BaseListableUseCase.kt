package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.anime.model.Entity

abstract class BaseListableUseCase {

    abstract fun invoke(id: Long): Result<List<Entity>>

}
