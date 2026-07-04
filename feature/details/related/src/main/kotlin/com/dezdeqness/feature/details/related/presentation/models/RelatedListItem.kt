package com.dezdeqness.feature.details.related.presentation.models

sealed interface RelatedListItem {
    val id: Long
    val name: String
    val imageUrl: String
    val briefInfo: String
}

data class ChronologyUiModel(
    override val id: Long,
    override val name: String,
    override val imageUrl: String,
    override val briefInfo: String,
) : RelatedListItem

data class SimilarUiModel(
    override val id: Long,
    override val name: String,
    override val imageUrl: String,
    override val briefInfo: String,
    val score: String,
) : RelatedListItem
