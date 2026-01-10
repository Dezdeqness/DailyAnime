package com.dezdeqness.shared.domain.model

data class StatusEntity(
    val id: Long,
    val groupedId: String,
    val name: String,
    val size: Long,
    val type: String
)
