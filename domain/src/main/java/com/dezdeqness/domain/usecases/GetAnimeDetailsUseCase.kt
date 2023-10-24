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

    operator fun invoke(id: Long): Result<AnimeDetailsFullEntity> {
        val token = accountRepository.getToken().accessToken

        val detailsResult = animeRepository.getDetails(id = id, token = token)
        detailsResult.onFailure { exception ->
            return Result.failure(exception)
        }

        val screenshotsResult = animeRepository.getScreenshots(id = id)
        screenshotsResult.onFailure { exception ->
            return Result.failure(exception)
        }

        val relatedItemsResult = animeRepository.getRelated(id = id)
        relatedItemsResult.onFailure { exception ->
            return Result.failure(exception)
        }

        val rolesResult = animeRepository.getRoles(id = id)
        rolesResult.onFailure { exception ->
            return Result.failure(exception)
        }

        val animeDetails = detailsResult.getOrElse {
            return Result.failure(Throwable("Anime details failure"))
        }
        val screenshots = screenshotsResult.getOrNull() ?: listOf()
        val relates = relatedItemsResult.getOrNull() ?: listOf()
        val roles = rolesResult.getOrNull() ?: listOf()

        if (token.isNotEmpty()) {
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
