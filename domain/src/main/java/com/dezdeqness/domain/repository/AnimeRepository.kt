package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.RelatedItemEntity
import com.dezdeqness.domain.model.RoleEntity
import com.dezdeqness.domain.model.ScreenshotEntity

interface AnimeRepository {

    fun getDetails(id: Long, token: String): Result<AnimeDetailsEntity>

    fun getScreenshots(id: Long): Result<List<ScreenshotEntity>>

    fun getRelated(id: Long): Result<List<RelatedItemEntity>>

    fun getRoles(id: Long): Result<List<RoleEntity>>

    fun getListWithFilter(
        queryMap: Map<String, String>,
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
    ): Result<List<AnimeBriefEntity>>

}
