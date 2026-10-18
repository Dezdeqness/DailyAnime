package com.dezdeqness.domain.anime.usecases

import com.dezdeqness.contract.anime.repository.AnimeChronologyRepository

class GetChronologyUseCase(
    private val repository: AnimeChronologyRepository,
) : BaseListableUseCase() {

    override suspend fun invoke(id: Long) = repository.getChronology(id)
}
