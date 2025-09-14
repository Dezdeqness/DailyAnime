package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.history.model.HistoryEntity
import com.dezdeqness.contract.history.repository.HistoryRepository

class GetLatestHistoryItemUseCase(
    private val historyRepository: HistoryRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Result<HistoryEntity?> {
        if (authRepository.isAuthorized().not()) {
            return Result.success(null)
        }

        val result = historyRepository.getUserHistory(
            page = PAGE,
            limit = PAGE_SIZE,
        )

        result.onFailure {
            return Result.failure(it)
        }
        val item = result
            .getOrDefault(listOf())
            .firstOrNull()

        return Result.success(item)
    }

    companion object {
        private const val PAGE_SIZE = 1
        private const val PAGE = 1
    }
}
