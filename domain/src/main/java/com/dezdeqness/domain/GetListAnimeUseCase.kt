package com.dezdeqness.domain

class GetListAnimeUseCase(
    private val animeRepository: AnimeRepository,
){

    operator fun invoke(): Result<List<AnimeEntity>> = animeRepository.getListAnime()

}
