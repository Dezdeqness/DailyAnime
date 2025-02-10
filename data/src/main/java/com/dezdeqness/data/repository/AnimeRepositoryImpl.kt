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

    override fun getDetails(id: Long, isAuthorized: Boolean): Result<AnimeDetailsEntity> =
        animeRemoteDataSource.getDetailsAnimeMainInfo(id, isAuthorized)

    override fun getScreenshots(id: Long): Result<List<ScreenshotEntity>> =
        animeRemoteDataSource.getDetailsAnimeScreenshots(id)

    override fun getRelated(id: Long): Result<List<RelatedItemEntity>> =
        animeRemoteDataSource.getDetailsAnimeRelated(id)

    override fun getRoles(id: Long): Result<List<RoleEntity>> =
        animeRemoteDataSource.getDetailsAnimeRoles(id)

    override fun getSimilar(id: Long): Result<List<AnimeBriefEntity>> =
        animeRemoteDataSource.getDetailsAnimeSimilar(id = id)

    override fun getChronology(id: Long): Result<List<AnimeChronologyEntity>> =
        animeRemoteDataSource.getDetailsChronology(id = id)

    override fun getListWithFilter(
        queryMap: Map<String, String>,
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
    ) =
        animeRemoteDataSource.getListAnime(queryMap, pageNumber, sizeOfPage, searchQuery)

}
