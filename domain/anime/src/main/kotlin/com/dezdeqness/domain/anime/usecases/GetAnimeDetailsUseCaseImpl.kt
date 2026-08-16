package com.dezdeqness.domain.anime.usecases

import com.dezdeqness.contract.anime.model.AnimeDetailsFullEntity
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.anime.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import javax.inject.Inject

class GetAnimeDetailsUseCaseImpl @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val authRepository: AuthRepository,
    private val userRatesRepository: UserRatesRepository,
) : GetAnimeDetailsUseCase {

    override suspend fun invoke(id: Long): Result<AnimeDetailsFullEntity> {
        val isAuthorized = authRepository.isAuthorized()

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
            ),
        )
    }
}
