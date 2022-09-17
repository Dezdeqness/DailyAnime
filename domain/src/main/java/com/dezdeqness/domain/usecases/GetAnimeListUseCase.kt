package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.model.AnimeEntity
import com.dezdeqness.domain.repository.AnimeRepository

class GetAnimeListUseCase(
    private val animeRepository: AnimeRepository,
){

    operator fun invoke(): Result<List<AnimeEntity>> = animeRepository.getListAnime()

}
