package com.dezdeqness.data.datasource

import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.contract.anime.model.RoleEntity
import com.dezdeqness.contract.anime.model.ScreenshotEntity

interface AnimeRemoteDataSource {

    fun getListAnime(
        queryMap: Map<String, String> = mapOf(),
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String,
        isAdultContentEnabled: Boolean,
    ): Result<List<AnimeBriefEntity>>

    fun getDetailsAnimeMainInfo(
        id: Long,
        isAuthorized: Boolean,
    ): Result<AnimeDetailsEntity>

    fun getDetailsAnimeScreenshots(
        id: Long,
    ): Result<List<ScreenshotEntity>>

    fun getDetailsAnimeRelated(
        id: Long,
    ): Result<List<RelatedItemEntity>>

    fun getDetailsAnimeRoles(
        id: Long,
    ): Result<List<RoleEntity>>

    fun getDetailsAnimeSimilar(
        id: Long,
    ): Result<List<AnimeBriefEntity>>

    fun getDetailsChronology(
        id: Long
    ): Result<List<AnimeChronologyEntity>>

    suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo>
}
