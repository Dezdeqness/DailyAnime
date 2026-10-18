package com.dezdeqness.data.shikimori.anime.repository

import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.repository.AnimeChronologyRepository
import com.dezdeqness.data.shikimori.anime.datasource.ShikimoriAnimeRemoteDataSource
import javax.inject.Inject

class ShikimoriAnimeChronologyRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: ShikimoriAnimeRemoteDataSource,
) : AnimeChronologyRepository {

    override suspend fun getChronology(id: Long): Result<List<AnimeChronologyEntity>> =
        animeRemoteDataSource.getDetailsChronology(id = id)
}
