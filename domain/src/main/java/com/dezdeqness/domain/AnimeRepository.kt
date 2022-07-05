package com.dezdeqness.domain

interface AnimeRepository {

    fun getListAnime(): Result<List<AnimeEntity>>

}
