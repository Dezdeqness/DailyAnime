package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.model.AnimeBriefEntity

interface AnimeRepository {

    fun getDetails(id: Long): Result<AnimeDetailsFullEntity>

    fun getListWithFilter(queryMap: Map<String, String>, pageNumber: Int, sizeOfPage: Int): Result<List<AnimeBriefEntity>>

}
