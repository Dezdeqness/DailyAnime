package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.contract.anime.model.RoleEntity
import com.dezdeqness.contract.anime.model.ScreenshotEntity
import com.dezdeqness.contract.anime.repository.AnimeRepository
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

    override suspend fun getAdditionalInfo(id: Long) = animeRemoteDataSource.getAdditionalInfo(id)

}
