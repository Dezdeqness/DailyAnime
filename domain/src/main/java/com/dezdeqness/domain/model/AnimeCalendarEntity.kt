package com.dezdeqness.domain.model

import com.dezdeqness.contract.anime.model.AnimeBriefEntity

data class AnimeCalendarEntity(
    val duration: Int,
    val nextEpisode: Int,
    val nextEpisodeAtTimestamp: Long,
    val anime: AnimeBriefEntity,
)
