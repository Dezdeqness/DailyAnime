package com.dezdeqness.contract.anime.repository

import com.dezdeqness.contract.anime.model.AnimeChronologyEntity

interface AnimeChronologyRepository {

    suspend fun getChronology(id: Long): Result<List<AnimeChronologyEntity>>
}
