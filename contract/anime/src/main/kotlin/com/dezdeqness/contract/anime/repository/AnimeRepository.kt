package com.dezdeqness.contract.anime.repository

import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity

interface AnimeRepository {

    fun getDetails(id: Long, isAuthorized: Boolean): Result<AnimeDetailsEntity>

    fun getSimilar(id: Long): Result<List<AnimeBriefEntity>>

    fun getChronology(id: Long): Result<List<AnimeChronologyEntity>>

    suspend fun getListWithFilter(
        queryMap: Map<String, String>,
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
    ): Result<List<AnimeBriefEntity>>

    suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo>
}
