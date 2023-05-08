package com.dezdeqness.data.model

import com.squareup.moshi.Json

class AnimeDetailsRemote : AnimeBriefRemote() {

    @field:Json(name = "english")
    val english: List<String> = listOf()

    @field:Json(name = "japanese")
    val japanese: List<String> = listOf()

    @field:Json(name = "duration")
    val duration: String = ""

    @field:Json(name = "rating")
    val rating: String = ""

    @field:Json(name = "description_html")
    val descriptionHTML: String = ""

    @field:Json(name = "description")
    val description: String? = null

    @field:Json(name = "genres")
    val genres: List<GenreRemote> = listOf()

    @field:Json(name = "studios")
    val studios: List<StudioRemote> = listOf()

    @field:Json(name = "videos")
    val videos: List<VideoRemote> = listOf()

    @field:Json(name = "next_episode_at")
    val nextEpisodeAt: String? = null

    @field:Json(name = "user_rate")
    val userRate: UserRateRemote? = null

    @field:Json(name = "rates_scores_stats")
    val ratesScoresStats: List<ScoreStatsItem>? = null

    @field:Json(name = "rates_statuses_stats")
    val ratesStatusesStats: List<StatusStatsItem>? = null
}

data class ScoreStatsItem(
    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "value")
    val value: Int,
)

data class StatusStatsItem(
    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "value")
    val value: Int,
)