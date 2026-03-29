package com.dezdeqness.feature.topicdetails.presentation

import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.feature.topicdetails.R
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedAnimeUiModel
import com.dezdeqness.foundation.provider.ResourceProvider
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import javax.inject.Inject

class LinkedAnimeComposer @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
    private val resourceProvider: ResourceProvider,
) {

    fun compose(anime: AnimeDetailsEntity): LinkedAnimeUiModel {
        val title = anime.russian.ifBlank { anime.name }

        return LinkedAnimeUiModel(
            id = anime.id,
            imageUrl = anime.image.original,
            title = title,
            status = mapStatus(anime.status),
            type = animeKindUtils.mapKind(anime.kind),
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
