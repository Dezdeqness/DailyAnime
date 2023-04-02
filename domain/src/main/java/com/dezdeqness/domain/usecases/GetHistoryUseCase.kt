package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.model.HistoryEntity
import com.dezdeqness.domain.repository.AccountRepository

class GetHistoryUseCase(
    private val accountRepository: AccountRepository,
) {

    operator fun invoke(pageNumber: Int): Result<HistoryListState> {
        val result = accountRepository.getUserHistory(
            page = pageNumber,
            limit = PAGE_SIZE,
        )
        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Throwable())
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
