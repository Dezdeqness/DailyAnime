package com.dezdeqness.data.anilist.anime.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Query
import com.dezdeqness.data.anilist.graphql.AnimeAdditionalInfoQuery
import com.dezdeqness.data.anilist.graphql.AnimeDetailsQuery
import com.dezdeqness.data.anilist.graphql.SearchAnimeQuery
import com.dezdeqness.data.anilist.remote.di.AnilistRemoteModule
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createGraphqlException
import javax.inject.Inject
import javax.inject.Named

class AnilistAnimeRemoteDataSourceImpl @Inject constructor(
    @Named(AnilistRemoteModule.GRAPHQL_CLIENT) private val apolloClient: ApolloClient,
) : BaseDataSource(), AnilistAnimeRemoteDataSource {

    override suspend fun search(query: SearchAnimeQuery) = execute(query) { it.Page }

    override suspend fun getDetails(query: AnimeDetailsQuery) = execute(query) { it.Media }

    override suspend fun getAdditionalInfo(query: AnimeAdditionalInfoQuery) = execute(query) { it.Media }

    private suspend fun <D : Query.Data, T : Any> execute(
        query: Query<D>,
        extract: (D) -> T?,
    ): Result<T> = tryWithCatchSuspend {
        val response = apolloClient.query(query).execute()
        val result = response.data?.let(extract)

        if (result != null && response.hasErrors().not()) {
            Result.success(result)
        } else {
            throw response.createGraphqlException()
        }
    }
}
