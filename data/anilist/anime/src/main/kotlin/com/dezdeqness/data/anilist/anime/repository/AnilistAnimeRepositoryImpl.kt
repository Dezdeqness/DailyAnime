package com.dezdeqness.data.anilist.anime.repository

import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.core.Page
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.anilist.anime.datasource.AnilistAnimeRemoteDataSource
import javax.inject.Inject

class AnilistAnimeRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: AnilistAnimeRemoteDataSource,
    private val settingsRepository: SettingsRepository,
) : AnimeRepository {

    override suspend fun getDetails(id: Long, isAuthorized: Boolean): Result<AnimeDetailsEntity> =
        animeRemoteDataSource.getDetailsAnimeMainInfo(id)

    override suspend fun search(
        params: AnimeSearchParams,
        page: Int,
        pageSize: Int,
    ): Result<Page<AnimeBriefEntity>> = animeRemoteDataSource.getListAnime(
        pageNumber = page,
        sizeOfPage = pageSize,
        searchQuery = params.text,
        isAdultContentHidden = settingsRepository.getPreference(AdultContentPreference),
    )

    override suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo> =
        animeRemoteDataSource.getAdditionalInfo(id)
}
