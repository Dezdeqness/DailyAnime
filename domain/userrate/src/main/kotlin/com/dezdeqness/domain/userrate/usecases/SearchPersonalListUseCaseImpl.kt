package com.dezdeqness.domain.userrate.usecases

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.anime.model.UserRateStatusEntity
import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import com.dezdeqness.contract.userrate.usecases.SearchPersonalListUseCase
import javax.inject.Inject

class SearchPersonalListUseCaseImpl @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
) : SearchPersonalListUseCase {

    override suspend fun invoke(search: String): Result<List<UserRateEntity>> = userRatesRepository.searchUserRates(
        search = search,
        statuses = getAllStatusesString(),
    )

    private fun getAllStatusesString(): String {
        return UserRateStatusEntity.entries
            .filter { it != UserRateStatusEntity.NONE && it != UserRateStatusEntity.UNKNOWN }
            .joinToString(separator = ",") { it.status }
    }
}
