package com.dezdeqness.domain.anime.usecases

import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.anime.usecases.GetAnimeListUseCase
import javax.inject.Inject

class GetAnimeListUseCaseImpl @Inject constructor(
    private val animeRepository: AnimeRepository,
) : GetAnimeListUseCase {

    override suspend fun invoke(pageNumber: Int, params: AnimeSearchParams) = animeRepository
        .search(params = params, page = pageNumber, pageSize = PAGE_SIZE)
        .map { page ->
            GetAnimeListUseCase.AnimeListState(
                list = page.items,
                hasNextPage = page.hasNext,
                currentPage = if (page.items.isEmpty()) pageNumber else pageNumber + 1,
            )
        }

    companion object {
        private const val PAGE_SIZE = 24
    }
}
