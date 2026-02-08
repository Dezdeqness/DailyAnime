package com.dezdeqness.feature.personallist.search.model

import com.dezdeqness.contract.anime.model.UserRateStatusEntity

data class SearchUserRateUiModel(
    val id: Long,
    val name: String,
    val kind: String,
    val episodes: Int,
    val logoUrl: String,
    val overallEpisodes: Int,
    val updatedAtTimestamp: Long,
    val status: UserRateStatusEntity,
)