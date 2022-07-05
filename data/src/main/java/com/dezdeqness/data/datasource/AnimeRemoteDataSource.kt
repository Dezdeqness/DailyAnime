package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.AnimeEntity

interface AnimeRemoteDataSource {

    fun getListAnime(): Result<List<AnimeEntity>>

}
