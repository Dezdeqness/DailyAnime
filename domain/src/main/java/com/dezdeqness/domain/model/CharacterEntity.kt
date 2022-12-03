package com.dezdeqness.domain.model

data class CharacterEntity(
    val id: Long,
    val name: String,
    val russian: String,
    val image: ImageEntity,
    val url: String,
)
