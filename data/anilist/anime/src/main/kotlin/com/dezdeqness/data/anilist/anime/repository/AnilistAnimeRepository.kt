package com.dezdeqness.data.anilist.anime.repository

import com.apollographql.apollo.api.Optional
import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.core.Page
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.anilist.anime.datasource.AnilistAnimeRemoteDataSource
import com.dezdeqness.data.anilist.anime.mapper.AnilistAnimeMapper
import com.dezdeqness.data.anilist.graphql.SearchAnimeQuery
import com.dezdeqness.data.anilist.graphql.type.MediaSort
import javax.inject.Inject

class AnilistAnimeRepository @Inject constructor(
    private val remoteDataSource: AnilistAnimeRemoteDataSource,
    private val mapper: AnilistAnimeMapper,
    private val settingsRepository: SettingsRepository,
) : AnimeRepository {

    override suspend fun search(
        params: AnimeSearchParams,
        page: Int,
        pageSize: Int,
    ): Result<Page<AnimeBriefEntity>> {
        val search = params.text.trim().ifEmpty { null }

        val isAdultHidden = settingsRepository.getPreference(AdultContentPreference)

        val query = SearchAnimeQuery(
            page = page,
            perPage = pageSize,
            search = Optional.presentIfNotNull(search),
            isAdult = if (isAdultHidden) Optional.present(false) else Optional.absent(),
            sort = Optional.present(listOf(if (search != null) MediaSort.SEARCH_MATCH else MediaSort.POPULARITY_DESC)),
        )

        return remoteDataSource.search(query).map { result ->
            Page(
                items = result.media.orEmpty().mapNotNull { it?.animeBrief }.map(mapper::map),
                hasNext = result.pageInfo?.hasNextPage == true,
            )
        }
    }

    override suspend fun getDetails(id: Long, isAuthorized: Boolean): Result<AnimeDetailsEntity> = unsupported()

    override suspend fun getSimilar(id: Long): Result<List<AnimeBriefEntity>> = unsupported()

    override suspend fun getChronology(id: Long): Result<List<AnimeChronologyEntity>> = unsupported()

    override suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo> = unsupported()

    private fun <T> unsupported(): Result<T> =
        Result.failure(UnsupportedOperationException("Not supported by AniList yet"))
}
