package com.dezdeqness.domain.history.usecases

import com.dezdeqness.contract.history.repository.HistoryRepository
import com.dezdeqness.contract.history.usecases.GetHistoryUseCase
import javax.inject.Inject

class GetHistoryUseCaseImpl @Inject constructor(
    private val historyRepository: HistoryRepository,
) : GetHistoryUseCase {

    override operator fun invoke(pageNumber: Int): Result<GetHistoryUseCase.HistoryListState> {
        val result = historyRepository.getUserHistory(
            page = pageNumber,
            limit = PAGE_SIZE,
        )
        result.onFailure {
            return Result.failure(it)
        }
        val list = result.getOrDefault(listOf())

        val hasNextPage = list.size >= PAGE_SIZE

        return Result.success(
            GetHistoryUseCase.HistoryListState(
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
