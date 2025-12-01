package com.dezdeqness.data.datasource

import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity

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

    fun getDetailsAnimeSimilar(
        id: Long,
    ): Result<List<AnimeBriefEntity>>

    fun getDetailsChronology(
        id: Long
    ): Result<List<AnimeChronologyEntity>>

    suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo>
}
