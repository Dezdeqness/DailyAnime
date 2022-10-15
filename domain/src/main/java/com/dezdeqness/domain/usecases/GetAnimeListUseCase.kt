package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.model.AnimeEntity
import com.dezdeqness.domain.repository.AnimeRepository

class GetAnimeListUseCase(
    private val animeRepository: AnimeRepository,
) {

    operator fun invoke(pageNumber: Int, queryMap: Map<String, String>): Result<AnimeListState> {
        val result = animeRepository.getListWithFilter(queryMap, pageNumber, PAGE_SIZE)
        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Throwable())
        }
        val list = result.getOrDefault(listOf())

        val hasNextPage = !(list.isEmpty() || list.size < PAGE_SIZE)

        return Result.success(
            AnimeListState(
                list = list,
                hasNextPage = hasNextPage,
                currentPage = if (list.isEmpty()) pageNumber else pageNumber + 1,
            )
        )

    }

    data class AnimeListState(
        val list: List<AnimeEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )

    companion object {
        private const val PAGE_SIZE = 24
    }
}
