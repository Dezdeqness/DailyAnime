package com.dezdeqness.contract.userrate.usecases

import com.dezdeqness.contract.anime.model.UserRateEntity

interface SearchPersonalListUseCase {

    suspend operator fun invoke(search: String): Result<List<UserRateEntity>>
}
