package com.dezdeqness.domain.anime.usecases

import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.anime.usecases.GetAnimeListUseCase
import javax.inject.Inject

class GetAnimeListUseCaseImpl @Inject constructor(
    private val animeRepository: AnimeRepository,
) : GetAnimeListUseCase {

    override suspend fun invoke(
        pageNumber: Int,
        queryMap: Map<String, String>,
        searchQuery: String,
    ): Result<GetAnimeListUseCase.AnimeListState> {
        val result = animeRepository.getListWithFilter(queryMap, pageNumber, PAGE_SIZE, searchQuery)
        result.onFailure {
            return Result.failure(it)
        }
        val list = result.getOrDefault(listOf())

        val hasNextPage = !(list.isEmpty() || list.size < PAGE_SIZE)

        return Result.success(
            GetAnimeListUseCase.AnimeListState(
                list = list,
                hasNextPage = hasNextPage,
                currentPage = if (list.isEmpty()) pageNumber else pageNumber + 1,
            ),
        )
    }

    companion object {
        private const val PAGE_SIZE = 24
    }
}
