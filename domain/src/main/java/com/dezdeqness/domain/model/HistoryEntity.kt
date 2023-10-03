package com.dezdeqness.domain.model

data class HistoryEntity(
    val id: Long,
    val description: String,
    val createdAtTimestamp: Long,
    val itemId: Long,
    val itemName: String,
    val itemRussian: String,
    val itemUrl: String,
    val image: String,
)
