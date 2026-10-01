package com.dezdeqness.data.anime.repository

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.core.Page
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.anime.datasource.AnimeRemoteDataSource
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

    override suspend fun search(
        params: AnimeSearchParams,
        page: Int,
        pageSize: Int,
    ): Result<Page<AnimeBriefEntity>> = animeRemoteDataSource
        .getListAnime(
            queryMap = params.filters.toQueryMap(),
            pageNumber = page,
            sizeOfPage = pageSize,
            searchQuery = params.text,
            isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference),
        )
        .map { items -> Page(items = items, hasNext = items.size >= pageSize) }

    override suspend fun getAdditionalInfo(id: Long) = animeRemoteDataSource.getAdditionalInfo(id)

    private fun Map<String, Set<String>>.toQueryMap() = filterValues { it.isNotEmpty() }
        .mapValues { (_, ids) -> ids.joinToString(",") }
}
