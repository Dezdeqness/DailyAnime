package com.dezdeqness.data.anilist.anime.datasource

import com.dezdeqness.data.anilist.graphql.SearchAnimeQuery

interface AnilistAnimeRemoteDataSource {

    suspend fun search(query: SearchAnimeQuery): Result<SearchAnimeQuery.Page>
}
