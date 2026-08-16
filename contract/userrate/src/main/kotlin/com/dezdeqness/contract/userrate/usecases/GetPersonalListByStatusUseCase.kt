package com.dezdeqness.contract.userrate.usecases

import com.dezdeqness.contract.anime.model.UserRateEntity

interface GetPersonalListByStatusUseCase {

    suspend operator fun invoke(page: Int, status: String): Result<GetPersonalListByStatusUseCase.PersonalListState>

    data class PersonalListState(
        val list: List<UserRateEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )
}
