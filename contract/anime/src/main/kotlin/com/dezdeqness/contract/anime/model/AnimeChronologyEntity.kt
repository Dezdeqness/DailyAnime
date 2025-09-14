package com.dezdeqness.contract.anime.model

data class AnimeChronologyEntity(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val url: String,
    val kind: String,
    val year: String,
): Entity
