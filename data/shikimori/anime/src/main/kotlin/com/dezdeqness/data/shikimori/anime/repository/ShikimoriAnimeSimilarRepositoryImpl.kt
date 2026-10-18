package com.dezdeqness.data.shikimori.anime.repository

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.repository.AnimeSimilarRepository
import com.dezdeqness.data.shikimori.anime.datasource.ShikimoriAnimeRemoteDataSource
import javax.inject.Inject

class ShikimoriAnimeSimilarRepositoryImpl @Inject constructor(
    private val animeRemoteDataSource: ShikimoriAnimeRemoteDataSource,
) : AnimeSimilarRepository {

    override suspend fun getSimilar(id: Long): Result<List<AnimeBriefEntity>> =
        animeRemoteDataSource.getDetailsAnimeSimilar(id = id)
}
