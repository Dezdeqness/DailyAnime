package com.dezdeqness.contract.anime.model

data class GenreEntity(
    val numericId: String,
    val id: String,
    val name: String,
    val type: TypeEntity,
    val kind: GenreKindEntity,
)
