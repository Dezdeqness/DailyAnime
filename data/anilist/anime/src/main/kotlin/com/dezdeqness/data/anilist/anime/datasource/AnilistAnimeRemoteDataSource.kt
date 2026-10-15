package com.dezdeqness.data.anilist.anime.datasource

import com.dezdeqness.data.anilist.graphql.AnimeAdditionalInfoQuery
import com.dezdeqness.data.anilist.graphql.AnimeDetailsQuery
import com.dezdeqness.data.anilist.graphql.SearchAnimeQuery

interface AnilistAnimeRemoteDataSource {

    suspend fun search(query: SearchAnimeQuery): Result<SearchAnimeQuery.Page>

    suspend fun getDetails(query: AnimeDetailsQuery): Result<AnimeDetailsQuery.Media>

    suspend fun getAdditionalInfo(query: AnimeAdditionalInfoQuery): Result<AnimeAdditionalInfoQuery.Media>
}
