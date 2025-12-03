package com.dezdeqness.data.repository

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: AnimeRemoteDataSource,
    private val settingsRepository: SettingsRepository,
) : AnimeRepository {

    override suspend fun getDetails(id: Long, isAuthorized: Boolean): Result<AnimeDetailsEntity> =
        animeRemoteDataSource.getDetailsAnimeMainInfo(id, isAuthorized)

    override suspend fun getSimilar(id: Long): Result<List<AnimeBriefEntity>> =
        animeRemoteDataSource.getDetailsAnimeSimilar(id = id)

    override suspend fun getChronology(id: Long): Result<List<AnimeChronologyEntity>> =
        animeRemoteDataSource.getDetailsChronology(id = id)

    override suspend fun getListWithFilter(
        queryMap: Map<String, String>,
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
    ) =
        animeRemoteDataSource.getListAnime(
            queryMap,
            pageNumber,
            sizeOfPage,
            searchQuery,
            isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference))

    override suspend fun getAdditionalInfo(id: Long) = animeRemoteDataSource.getAdditionalInfo(id)

}
