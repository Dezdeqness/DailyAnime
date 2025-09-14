package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.history.model.HistoryEntity
import com.dezdeqness.contract.history.repository.HistoryRepository

class GetHistoryUseCase(
    private val historyRepository: HistoryRepository,
) {

    operator fun invoke(pageNumber: Int): Result<HistoryListState> {
        val result = historyRepository.getUserHistory(
            page = pageNumber,
            limit = PAGE_SIZE,
        )
        result.onFailure {
            return Result.failure(it)
        }
        val list = result.getOrDefault(listOf())

        val hasNextPage = list.size > PAGE_SIZE || list.isNotEmpty()

        return Result.success(
            HistoryListState(
                list = list,
                hasNextPage = hasNextPage,
                currentPage = if (list.isEmpty()) pageNumber else pageNumber + 1,
            )
        )
    }

    data class HistoryListState(
        val list: List<HistoryEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )

    companion object {
        private const val PAGE_SIZE = 24
    }
}
