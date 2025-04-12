package com.dezdeqness.domain.usecases

import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.domain.repository.UserRatesRepository

class GetAnimeDetailsUseCase(
    private val animeRepository: AnimeRepository,
    private val accountRepository: AccountRepository,
    private val userRatesRepository: UserRatesRepository,
) {

    suspend operator fun invoke(id: Long): Result<AnimeDetailsFullEntity> {
        val isAuthorized = accountRepository.isAuthorized()

        val detailsResult = animeRepository.getDetails(id = id, isAuthorized = isAuthorized)
        detailsResult.onFailure { exception ->
            return Result.failure(exception)
        }

        val additionalInfoResult = animeRepository.getAdditionalInfo(id = id)
        additionalInfoResult.onFailure { exception ->
            return Result.failure(exception)
        }

        val animeDetails = detailsResult.getOrElse {
            return Result.failure(it)
        }
        val screenshots = additionalInfoResult.getOrNull()?.first ?: listOf()
        val relates = additionalInfoResult.getOrNull()?.second ?: listOf()
        val roles = additionalInfoResult.getOrNull()?.third ?: listOf()

        if (isAuthorized) {
            animeDetails.userRate?.let { userRatesRepository.updateLocalUserRate(it) }
        }

        return Result.success(
            AnimeDetailsFullEntity(
                animeDetailsEntity = animeDetails,
                screenshots = screenshots,
                relates = relates,
                roles = roles,
            )
        )
    }

}
