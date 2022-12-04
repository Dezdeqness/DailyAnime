package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.repository.AnimeRepository
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: AnimeRemoteDataSource,
) : AnimeRepository {

    override fun getDetails(id: Long): Result<AnimeDetailsFullEntity> {
        val mainInfoResult = animeRemoteDataSource.getDetailsAnimeMainInfo(id)
        if (mainInfoResult.isFailure) {
            return Result.failure(mainInfoResult.exceptionOrNull() ?: Throwable())
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
