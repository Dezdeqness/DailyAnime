package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.anime.model.UserRateStatusEntity
import com.dezdeqness.domain.repository.UserRatesRepository

class SearchPersonalListUseCase(
    private val userRatesRepository: UserRatesRepository,
) {
    suspend operator fun invoke(
        search: String,
    ) = userRatesRepository.searchUserRates(
        search = search,
        statuses = getAllStatusesString(),
    )

    private fun getAllStatusesString(): String {
        return UserRateStatusEntity.entries
            .filter { it != UserRateStatusEntity.NONE && it != UserRateStatusEntity.UNKNOWN }
            .joinToString(separator = ",") { it.status }
    }
}
