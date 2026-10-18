package com.dezdeqness.data.shikimori.anime.repository

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.core.Page
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.shikimori.anime.datasource.ShikimoriAnimeRemoteDataSource
import javax.inject.Inject

class ShikimoriAnimeRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: ShikimoriAnimeRemoteDataSource,
    private val settingsRepository: SettingsRepository,
) : AnimeRepository {

    override suspend fun getDetails(id: Long, isAuthorized: Boolean): Result<AnimeDetailsEntity> =
        animeRemoteDataSource.getDetailsAnimeMainInfo(id, isAuthorized)

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
            isAdultContentHidden = settingsRepository.getPreference(AdultContentPreference),
        )
        .map { items -> Page(items = items, hasNext = items.size >= pageSize) }

    override suspend fun getAdditionalInfo(id: Long) = animeRemoteDataSource.getAdditionalInfo(id)

    private fun Map<String, Set<String>>.toQueryMap() = filterValues { it.isNotEmpty() }
        .mapValues { (_, ids) -> ids.joinToString(",") }
}
