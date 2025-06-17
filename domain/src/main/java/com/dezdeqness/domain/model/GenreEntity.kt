package com.dezdeqness.domain.model

data class GenreEntity(
    val numericId: String,
    val id: String,
    val name: String,
    val type: TypeEntity,
    val kind: GenreKindEntity,
)
