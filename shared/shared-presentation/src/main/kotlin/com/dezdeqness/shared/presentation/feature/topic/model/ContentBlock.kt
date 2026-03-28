package com.dezdeqness.shared.presentation.feature.topic.model

sealed class ContentBlock {

    data class Text(
        val text: String,
        val bold: Boolean = false,
        val italic: Boolean = false,
        val underline: Boolean = false,
    ) : ContentBlock()

    data class Link(
        val text: String,
        val url: String,
        val entityType: String? = null,
        val entityId: Long? = null,
    ) : ContentBlock()

    data class Quote(
        val blocks: List<ContentBlock>,
    ) : ContentBlock()

    data class Video(
        val thumbnailUrl: String,
        val videoUrl: String,
        val title: String,
        val platform: String,
    ) : ContentBlock()

    data class Image(
        val previewUrl: String,
        val originalUrl: String,
    ) : ContentBlock()

    data object ParagraphBreak : ContentBlock()

    data object LineBreak : ContentBlock()
}
