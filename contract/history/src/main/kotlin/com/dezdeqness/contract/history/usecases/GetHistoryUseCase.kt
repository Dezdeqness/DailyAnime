package com.dezdeqness.contract.history.usecases

import com.dezdeqness.contract.history.model.HistoryEntity

interface GetHistoryUseCase {

    operator fun invoke(pageNumber: Int): Result<GetHistoryUseCase.HistoryListState>

    data class HistoryListState(
        val list: List<HistoryEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )
}
