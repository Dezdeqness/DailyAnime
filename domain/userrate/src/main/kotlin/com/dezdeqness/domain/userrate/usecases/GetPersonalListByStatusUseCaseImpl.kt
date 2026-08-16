package com.dezdeqness.domain.userrate.usecases

import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import com.dezdeqness.contract.userrate.usecases.GetPersonalListByStatusUseCase
import javax.inject.Inject

class GetPersonalListByStatusUseCaseImpl @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
) : GetPersonalListByStatusUseCase {

    override suspend fun invoke(page: Int, status: String): Result<GetPersonalListByStatusUseCase.PersonalListState> {
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
            GetPersonalListByStatusUseCase.PersonalListState(
                list = list,
                hasNextPage = hasNextPage,
                currentPage = if (list.isEmpty()) page else page + 1,
            ),
        )
    }

    companion object {
        private const val PAGE_SIZE = 50
    }
}
