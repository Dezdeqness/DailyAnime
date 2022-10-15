package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AnimeEntity

interface AnimeRepository {

    fun getListWithFilter(queryMap: Map<String, String>, pageNumber: Int, sizeOfPage: Int): Result<List<AnimeEntity>>

}
