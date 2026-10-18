package com.dezdeqness.domain.anime.usecases

import com.dezdeqness.contract.anime.repository.AnimeSimilarRepository

class GetSimilarListUseCase(
    private val repository: AnimeSimilarRepository,
) : BaseListableUseCase() {

    override suspend fun invoke(id: Long) = repository.getSimilar(id)
}
