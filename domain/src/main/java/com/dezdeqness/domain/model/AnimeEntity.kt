package com.dezdeqness.domain.model

data class AnimeEntity(
    val id: Int,
    val name: String,
    val russian: String,
    val images: Map<String, String>,
    val url: String,
    val kind: AnimeKindEntity,
    val score: String,
    val status: AnimeStatusEntity,
    val episodes: Int,
    val episodesAired: Int,
    val airedOn: String,
    val releasedOn: String,
)
