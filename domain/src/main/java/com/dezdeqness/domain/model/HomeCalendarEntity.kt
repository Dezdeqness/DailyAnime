package com.dezdeqness.domain.model

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.contract.anime.model.ImageEntity

class HomeCalendarEntity(
    override val id: Long,
    override val name: String,
    override val russian: String,
    override val score: Float,
    override val kind: AnimeKind,
    override val url: String,
    override val status: AnimeStatus,
    override val airedOnTimestamp: Long,
    override val releasedOnTimestamp: Long,
    override val episodes: Int,
    override val episodesAired: Int,
    override val image: ImageEntity,
    override val nextEpisodeTimestamp: Long,
    val description: String?,
) : AnimeBriefEntity(
    id = id,
    name = name,
    russian = russian,
    image = image,
    url = url,
    kind = kind,
    score = score,
    status = status,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOnTimestamp = airedOnTimestamp,
    releasedOnTimestamp = releasedOnTimestamp,
    nextEpisodeTimestamp = nextEpisodeTimestamp,
)
