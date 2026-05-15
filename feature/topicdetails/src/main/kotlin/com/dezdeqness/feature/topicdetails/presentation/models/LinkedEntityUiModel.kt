package com.dezdeqness.feature.topicdetails.presentation.models

sealed interface LinkedEntityUiModel {
    val id: Long
    val imageUrl: String
    val title: String

    data class Anime(
        override val id: Long,
        override val imageUrl: String,
        override val title: String,
        val status: String,
        val type: String,
    ) : LinkedEntityUiModel

    data class Character(
        override val id: Long,
        override val imageUrl: String,
        override val title: String,
        val url: String,
    ) : LinkedEntityUiModel
}
