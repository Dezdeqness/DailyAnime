package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.domain.repository.UserRatesRepository

class GetPersonalListByStatusUseCase(
    private val userRatesRepository: UserRatesRepository,
) {
    suspend operator fun invoke(page: Int, status: String): Result<PersonalListState> {
        val result = userRatesRepository.getUserRates(
            page = page,
            limit = PAGE_SIZE,
            status = status,
        )
        result.onFailure {
            return Result.failure(it)
        }
        val list = result.getOrDefault(listOf())

        val hasNextPage = list.size >= PAGE_SIZE

        return Result.success(
            PersonalListState(
                list = list,
                hasNextPage = hasNextPage,
                currentPage = if (list.isEmpty()) page else page + 1,
            )
        )
    }

    data class PersonalListState(
        val list: List<UserRateEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )

    companion object {
        private const val PAGE_SIZE = 50
    }
}