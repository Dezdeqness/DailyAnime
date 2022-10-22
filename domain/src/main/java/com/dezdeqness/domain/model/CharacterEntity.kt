package com.dezdeqness.domain.model

data class CharacterEntity(
    val id: Long,
    val name: String,
    val russian: String,
    val images: Map<String, String>,
    val url: String,
)
