package com.dezdeqness.feature.calendar.data

import com.dezdeqness.data.model.AnimeBriefRemote
import com.squareup.moshi.Json

internal data class AnimeCalendarRemote(

    @field:Json(name = "duration")
    val duration: Int? = null,

    @field:Json(name = "next_episode")
    val nextEpisode: Int? = null,

    @field:Json(name = "next_episode_at")
    val nextEpisodeAt: String? = null,

    @field:Json(name = "anime")
    val anime: AnimeBriefRemote? = null,
)
