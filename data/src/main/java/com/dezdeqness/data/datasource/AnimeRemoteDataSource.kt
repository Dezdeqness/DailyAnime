package com.dezdeqness.data.datasource

import com.dezdeqness.domain.AnimeEntity

interface AnimeRemoteDataSource {

    fun getListAnime(): Result<List<AnimeEntity>>

}
