package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.data.datasource.db.UserRatesLocalDataSource
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import java.lang.Exception
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: AnimeRemoteDataSource,
    private val userRatesLocalDataSource: UserRatesLocalDataSource,
    private val tokenManager: TokenManager,
) : AnimeRepository {

    override fun getDetails(id: Long): Result<AnimeDetailsFullEntity> {
        val tokenData = tokenManager.getTokenData()

        val mainInfoResult = animeRemoteDataSource.getDetailsAnimeMainInfo(id, tokenData.accessToken)
        if (mainInfoResult.isFailure) {
            return Result.failure(mainInfoResult.exceptionOrNull() ?: Throwable())
        }

        if (tokenData.accessToken.isNotEmpty()) {
            mainInfoResult.getOrNull()?.let {
                it.userRate?.let {
                    userRatesLocalDataSource.saveUserRates(listOf(it))
                }
            }
        }

        val screenshotsResult = animeRemoteDataSource.getDetailsAnimeScreenshots(id)
        if (screenshotsResult.isFailure) {
            return Result.failure(screenshotsResult.exceptionOrNull() ?: Throwable())
        }

        val relatedItemsResult = animeRemoteDataSource.getDetailsAnimeRelated(id)
        if (relatedItemsResult.isFailure) {
            return Result.failure(relatedItemsResult.exceptionOrNull() ?: Throwable())
        }

        val rolesResult = animeRemoteDataSource.getDetailsAnimeRoles(id)
        if (rolesResult.isFailure) {
            return Result.failure(rolesResult.exceptionOrNull() ?: Throwable())
        }

        val animeDetails = mainInfoResult.getOrElse {
            return Result.failure(Throwable())
        }
        val screenshots = screenshotsResult.getOrNull() ?: listOf()
        val relates = relatedItemsResult.getOrNull() ?: listOf()
        val roles = rolesResult.getOrNull() ?: listOf()

        return Result.success(
            AnimeDetailsFullEntity(
                animeDetailsEntity = animeDetails,
                screenshots = screenshots,
                relates = relates,
                roles = roles,
            )
        )

    }

    override fun getListWithFilter(
        queryMap: Map<String, String>,
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
    ) =
        animeRemoteDataSource.getListAnime(queryMap, pageNumber, sizeOfPage, searchQuery)

}
