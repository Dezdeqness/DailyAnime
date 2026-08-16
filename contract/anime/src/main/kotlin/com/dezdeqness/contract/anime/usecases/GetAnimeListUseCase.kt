package com.dezdeqness.contract.anime.usecases

import com.dezdeqness.contract.anime.model.AnimeBriefEntity

interface GetAnimeListUseCase {

    suspend operator fun invoke(
        pageNumber: Int,
        queryMap: Map<String, String>,
        searchQuery: String,
    ): Result<GetAnimeListUseCase.AnimeListState>

    data class AnimeListState(
        val list: List<AnimeBriefEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )
}
