package com.dezdeqness.contract.anime.repository

import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.core.Page

interface AnimeRepository {

    suspend fun getDetails(id: Long, isAuthorized: Boolean): Result<AnimeDetailsEntity>

    suspend fun getSimilar(id: Long): Result<List<AnimeBriefEntity>>

    suspend fun getChronology(id: Long): Result<List<AnimeChronologyEntity>>

    suspend fun search(params: AnimeSearchParams, page: Int, pageSize: Int): Result<Page<AnimeBriefEntity>>

    suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo>
}
