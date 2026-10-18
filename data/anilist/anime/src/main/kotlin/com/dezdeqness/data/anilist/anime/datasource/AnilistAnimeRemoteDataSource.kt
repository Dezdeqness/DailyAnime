package com.dezdeqness.data.anilist.anime.datasource

import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.core.Page

interface AnilistAnimeRemoteDataSource {

    suspend fun getListAnime(
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String,
        isAdultContentHidden: Boolean,
    ): Result<Page<AnimeBriefEntity>>

    suspend fun getDetailsAnimeMainInfo(id: Long): Result<AnimeDetailsEntity>

    suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo>
}
