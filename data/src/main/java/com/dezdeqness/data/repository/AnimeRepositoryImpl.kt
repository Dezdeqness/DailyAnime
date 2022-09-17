package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AnimeRemoteDataSource
import com.dezdeqness.domain.repository.AnimeRepository
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: AnimeRemoteDataSource,
) : AnimeRepository {

    override fun getListAnime() =
        animeRemoteDataSource.getListAnime()

    override fun getListAnimeWithQuery(queryMap: Map<String, String>) =
        animeRemoteDataSource.getListAnime(queryMap)

}
