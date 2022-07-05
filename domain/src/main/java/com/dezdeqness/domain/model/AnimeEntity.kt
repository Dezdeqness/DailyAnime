package com.dezdeqness.domain.model

data class AnimeEntity(
    val id: Int,
    val name: String,
    val russian: String,
    val images: Map<String, String>,
    val url: String,
    // TODO: ENUM
    val kind: String,
    val score: String,
    // TODO: ENUM
    val status: String,
    val episodes: Int,
    val episodesAired: Int,
    val airedOn: String,
    val releasedOn: String,
)
