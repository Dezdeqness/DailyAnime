package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AnimeRemote
import com.dezdeqness.domain.model.AnimeEntity
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import javax.inject.Inject

class AnimeMapper @Inject constructor() {

    fun fromResponse(item: AnimeRemote) =
        AnimeEntity(
            id = item.id,
            name = item.name,
            russian = item.russian,
            images = item.images ?: mapOf(),
            url = item.url,
            kind = AnimeKind.valueOfKind(item.kind),
            score = item.score,
            status = AnimeStatus.valueOfStatus(item.status),
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOn = item.airedOn.orEmpty(),
            releasedOn = item.releasedOn.orEmpty(),
        )

}
