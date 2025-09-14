package com.dezdeqness.contract.anime.model

data class VideoEntity(
    val id: Int,
    val name: String,
    val playerUrl: String,
    val url: String,
    val imageUrl: String,
    val kind: String,
    val hosting: String,
)
