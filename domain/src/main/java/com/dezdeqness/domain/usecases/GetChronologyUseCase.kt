package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.repository.AnimeRepository

class GetChronologyUseCase(
    private val animeRepository: AnimeRepository,
) : BaseListableUseCase() {

    override fun invoke(id: Long) = animeRepository.getChronology(id)
}
