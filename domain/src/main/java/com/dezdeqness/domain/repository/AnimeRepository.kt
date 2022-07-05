package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AnimeEntity

interface AnimeRepository {

    fun getListAnime(): Result<List<AnimeEntity>>

}
