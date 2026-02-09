package com.dezdeqness.feature.personallist.search

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.anime.model.UserRateStatusEntity
import com.dezdeqness.feature.personallist.search.model.SearchUserRateUiModel
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchUserRateUiMapper @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
) {

    fun map(userRateEntities: List<UserRateEntity>) = userRateEntities.mapNotNull(::mapEntity)

    private fun mapEntity(userRateEntity: UserRateEntity): SearchUserRateUiModel? {
        val anime = userRateEntity.anime ?: return null

        val kind = animeKindUtils.mapKind(anime.kind)

        return SearchUserRateUiModel(
            id = anime.id,
            status = UserRateStatusEntity.fromString(userRateEntity.status),
            updatedAtTimestamp = userRateEntity.updatedAtTimestamp,
            name = anime.name,
            kind = kind,
            logoUrl = anime.image.original,
        )
    }
}
