package com.dezdeqness.contract.userrate.usecases

import com.dezdeqness.contract.anime.model.UserRateEntity

interface CreateOrUpdateUserRateUseCase {

    operator fun invoke(
        rateId: Long,
        targetId: String,
        status: String,
        episodes: Long,
        score: Float,
        comment: String,
    ): Result<UserRateEntity>
}
