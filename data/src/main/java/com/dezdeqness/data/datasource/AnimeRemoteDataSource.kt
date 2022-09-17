package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.AnimeEntity

interface AnimeRemoteDataSource {

    fun getListAnime(queryMap: Map<String, String> = mapOf()): Result<List<AnimeEntity>>

}
