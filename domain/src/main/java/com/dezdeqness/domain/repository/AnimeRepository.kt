package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AnimeEntity

interface AnimeRepository {

    fun getListAnime(): Result<List<AnimeEntity>>

    fun getListAnimeWithQuery(queryMap: Map<String, String>): Result<List<AnimeEntity>>

}
