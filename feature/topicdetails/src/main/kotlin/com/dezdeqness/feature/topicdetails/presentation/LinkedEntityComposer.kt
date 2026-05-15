package com.dezdeqness.feature.topicdetails.presentation

import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.domain.model.CharacterDetailsEntity
import com.dezdeqness.feature.topicdetails.R
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityUiModel
import com.dezdeqness.foundation.provider.ResourceProvider
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import javax.inject.Inject

class LinkedEntityComposer @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
    private val resourceProvider: ResourceProvider,
) {

    fun compose(anime: AnimeDetailsEntity): LinkedEntityUiModel.Anime {
        val title = anime.russian.ifBlank { anime.name }

        return LinkedEntityUiModel.Anime(
            id = anime.id,
            imageUrl = anime.image.original,
            title = title,
            status = mapStatus(anime.status),
            type = animeKindUtils.mapKind(anime.kind),
        )
    }

    fun compose(character: CharacterDetailsEntity): LinkedEntityUiModel.Character {
        val title = character.russian.ifBlank { character.name }

        return LinkedEntityUiModel.Character(
            id = character.id,
            imageUrl = character.image.original,
            title = title,
            url = character.url,
        )
    }

    private fun mapStatus(status: AnimeStatus): String {
        return when (status) {
            AnimeStatus.ANONS -> resourceProvider.getString(R.string.topic_details_status_anons)
            AnimeStatus.ONGOING -> resourceProvider.getString(R.string.topic_details_status_ongoing)
            AnimeStatus.RELEASED -> resourceProvider.getString(R.string.topic_details_status_released)
            AnimeStatus.LATEST -> resourceProvider.getString(R.string.topic_details_status_latest)
            AnimeStatus.UNKNOWN -> resourceProvider.getString(R.string.topic_details_status_unknown)
        }
    }
}
