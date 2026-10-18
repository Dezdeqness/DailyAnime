package com.dezdeqness.contract.anime.repository

import com.dezdeqness.contract.anime.model.AnimeBriefEntity

interface AnimeSimilarRepository {

    suspend fun getSimilar(id: Long): Result<List<AnimeBriefEntity>>
}
