package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.repository.AnimeRepository

class GetAnimeDetailsUseCase(
    private val animeRepository: AnimeRepository,
) {

    operator fun invoke(id: Long) = animeRepository.getDetails(id)

}
