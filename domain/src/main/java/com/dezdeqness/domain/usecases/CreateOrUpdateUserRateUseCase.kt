package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.repository.UserRatesRepository

class CreateOrUpdateUserRateUseCase(
    private val userRatesRepository: UserRatesRepository,
) {

    operator fun invoke(
        rateId: Long,
        targetId: String,
        status: String,
        episodes: Long,
        score: Float,
        comment: String,
    ): Result<Boolean> {
        val localUserRate = userRatesRepository.getLocalUserRate(rateId = rateId)
        return if (localUserRate == null) {
            userRatesRepository.createUserRate(
                targetId = targetId,
                status = status,
                episodes = episodes,
                score = score,
                comment = comment,
            )
        } else {
            userRatesRepository.updateUserRate(
                rateId = rateId,
                status = status,
                episodes = episodes,
                score = score,
                comment = comment,
            )
        }
    }

}
