package com.dezdeqness.data.anilist.anime.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.Query
import com.dezdeqness.contract.anime.DetailsAdditionalInfo
import com.dezdeqness.contract.core.Page
import com.dezdeqness.data.anilist.anime.mapper.AnilistAnimeDetailsMapper
import com.dezdeqness.data.anilist.anime.mapper.AnilistAnimeMapper
import com.dezdeqness.data.anilist.graphql.AnimeAdditionalInfoQuery
import com.dezdeqness.data.anilist.graphql.AnimeDetailsQuery
import com.dezdeqness.data.anilist.graphql.SearchAnimeQuery
import com.dezdeqness.data.anilist.graphql.type.MediaSort
import com.dezdeqness.data.anilist.remote.di.AnilistRemoteModule
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createGraphqlException
import javax.inject.Inject
import javax.inject.Named

class AnilistAnimeRemoteDataSourceImpl @Inject constructor(
    @Named(AnilistRemoteModule.GRAPHQL_CLIENT) private val apolloClient: ApolloClient,
    private val animeMapper: AnilistAnimeMapper,
    private val detailsMapper: AnilistAnimeDetailsMapper,
) : BaseDataSource(), AnilistAnimeRemoteDataSource {

    override suspend fun getListAnime(
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String,
        isAdultContentHidden: Boolean,
    ) = tryWithCatchSuspend {
        val search = searchQuery.trim().ifEmpty { null }
        val query = SearchAnimeQuery(
            page = pageNumber,
            perPage = sizeOfPage,
            search = Optional.presentIfNotNull(search),
            isAdult = if (isAdultContentHidden) Optional.present(false) else Optional.absent(),
            sort = Optional.present(listOf(if (search != null) MediaSort.SEARCH_MATCH else MediaSort.POPULARITY_DESC)),
        )

        execute(query) { it.Page }.map { page ->
            Page(
                items = page.media.orEmpty().mapNotNull { it?.animeBrief }.map(animeMapper::map),
                hasNext = page.pageInfo?.hasNextPage == true,
            )
        }
    }

    override suspend fun getDetailsAnimeMainInfo(id: Long) = tryWithCatchSuspend {
        execute(AnimeDetailsQuery(id = id.toInt())) { it.Media }.map(detailsMapper::mapDetails)
    }

    override suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo> = tryWithCatchSuspend {
        val query = AnimeAdditionalInfoQuery(id = id.toInt(), charactersPerPage = CHARACTERS_PER_PAGE)

        execute(query) { it.Media }.map { media ->
            val related = media.relations?.edges.orEmpty()
                .mapNotNull { it?.animeRelation }
                .mapNotNull(detailsMapper::mapRelation)
            val characters = media.characters?.edges.orEmpty()
                .mapNotNull { it?.animeCharacter }
                .mapNotNull(detailsMapper::mapRole)

            Triple(emptyList(), related, characters)
        }
    }

    private suspend fun <D : Query.Data, T : Any> execute(
        query: Query<D>,
        extract: (D) -> T?,
    ): Result<T> {
        val response = apolloClient.query(query).execute()
        val result = response.data?.let(extract)

        if (result == null || response.hasErrors()) throw response.createGraphqlException()

        return Result.success(result)
    }

    private companion object {
        const val CHARACTERS_PER_PAGE = 25
    }
}
