package com.dezdeqness.feature.personallist.search.model

import com.dezdeqness.contract.anime.model.UserRateStatusEntity

data class SearchUserRateUiModel(
    val id: Long,
    val name: String,
    val kind: String,
    val logoUrl: String,
    val updatedAtTimestamp: Long,
    val status: UserRateStatusEntity,
)
