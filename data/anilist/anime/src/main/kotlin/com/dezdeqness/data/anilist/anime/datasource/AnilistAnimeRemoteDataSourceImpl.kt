package com.dezdeqness.data.anilist.anime.datasource

import com.apollographql.apollo.ApolloClient
import com.dezdeqness.data.anilist.graphql.SearchAnimeQuery
import com.dezdeqness.data.anilist.remote.di.AnilistRemoteModule
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createGraphqlException
import javax.inject.Inject
import javax.inject.Named

class AnilistAnimeRemoteDataSourceImpl @Inject constructor(
    @Named(AnilistRemoteModule.GRAPHQL_CLIENT) private val apolloClient: ApolloClient,
) : BaseDataSource(), AnilistAnimeRemoteDataSource {

    override suspend fun search(query: SearchAnimeQuery) = tryWithCatchSuspend {
        val response = apolloClient.query(query).execute()
        val page = response.data?.Page

        if (page != null && response.hasErrors().not()) {
            Result.success(page)
        } else {
            throw response.createGraphqlException()
        }
    }
}
