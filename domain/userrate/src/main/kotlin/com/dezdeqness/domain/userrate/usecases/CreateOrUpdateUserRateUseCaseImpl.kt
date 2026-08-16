package com.dezdeqness.domain.userrate.usecases

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import com.dezdeqness.contract.userrate.usecases.CreateOrUpdateUserRateUseCase
import javax.inject.Inject

class CreateOrUpdateUserRateUseCaseImpl @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
) : CreateOrUpdateUserRateUseCase {

    override fun invoke(
        rateId: Long,
        targetId: String,
        status: String,
        episodes: Long,
        score: Float,
        comment: String,
    ): Result<UserRateEntity> {
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
