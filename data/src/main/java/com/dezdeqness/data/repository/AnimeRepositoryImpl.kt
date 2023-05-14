package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeChronologyEntity
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.RelatedItemEntity
import com.dezdeqness.domain.model.RoleEntity
import com.dezdeqness.domain.model.ScreenshotEntity
import com.dezdeqness.domain.repository.AnimeRepository
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: AnimeRemoteDataSource,
) : AnimeRepository {

    override fun getDetails(id: Long, token: String): Result<AnimeDetailsEntity> =
        animeRemoteDataSource
            .getDetailsAnimeMainInfo(id, token)
            .onFailure { return Result.failure(it) }

    override fun getScreenshots(id: Long): Result<List<ScreenshotEntity>> =
        animeRemoteDataSource
            .getDetailsAnimeScreenshots(id)
            .onFailure { return Result.failure(it) }

    override fun getRelated(id: Long): Result<List<RelatedItemEntity>> =
        animeRemoteDataSource
            .getDetailsAnimeRelated(id)
            .onFailure { return Result.failure(it) }

    override fun getRoles(id: Long): Result<List<RoleEntity>> =
        animeRemoteDataSource
            .getDetailsAnimeRoles(id)
            .onFailure { return Result.failure(it) }

    override fun getSimilar(id: Long): Result<List<AnimeBriefEntity>> =
        animeRemoteDataSource
            .getDetailsAnimeSimilar(id = id)
            .onFailure { return Result.failure(it) }

    override fun getChronology(id: Long): Result<List<AnimeChronologyEntity>> =
        animeRemoteDataSource
            .getDetailsChronology(id = id)
            .onFailure { return Result.failure(it) }

    override fun getListWithFilter(
        queryMap: Map<String, String>,
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
    ) =
        animeRemoteDataSource.getListAnime(queryMap, pageNumber, sizeOfPage, searchQuery)

}
