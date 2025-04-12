package com.dezdeqness.data.datasource

import com.dezdeqness.domain.DetailsAdditionalInfo
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeChronologyEntity
import com.dezdeqness.domain.model.RelatedItemEntity
import com.dezdeqness.domain.model.RoleEntity
import com.dezdeqness.domain.model.ScreenshotEntity

interface AnimeRemoteDataSource {

    fun getListAnime(
        queryMap: Map<String, String> = mapOf(),
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
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
