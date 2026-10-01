package com.dezdeqness.contract.anime.usecases

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeSearchParams

interface GetAnimeListUseCase {

    suspend operator fun invoke(pageNumber: Int, params: AnimeSearchParams): Result<GetAnimeListUseCase.AnimeListState>

    data class AnimeListState(
        val list: List<AnimeBriefEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )
}
