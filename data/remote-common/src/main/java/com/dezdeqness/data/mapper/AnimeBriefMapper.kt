package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.model.ImageRemote
import com.dezdeqness.data.util.TimestampConverter
import javax.inject.Inject

class AnimeBriefMapper @Inject constructor(
    private val timestampConverter: TimestampConverter,
) {

    fun fromResponse(item: AnimeBriefRemote) = AnimeBriefEntity(
        id = item.id,
        name = item.name,
        russian = item.russian,
        image = fromResponse(item.image),
        url = item.url,
        kind = AnimeKind.fromString(item.kind),
        score = item.score,
        status = AnimeStatus.fromString(item.status),
        episodes = item.episodes,
        episodesAired = item.episodesAired,
        airedOnTimestamp = timestampConverter.convertToTimeStamp(item.airedOn),
        releasedOnTimestamp = timestampConverter.convertToTimeStamp(item.releasedOn),
    )

    private fun fromResponse(image: ImageRemote?) = ImageEntity(
        original = image?.original.orEmpty(),
        preview = image?.preview.orEmpty(),
        x96 = image?.x96.orEmpty(),
        x48 = image?.x48.orEmpty(),
    )
}
