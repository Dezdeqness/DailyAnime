package com.dezdeqness.feature.news.presentation.models

sealed class ParagraphBlock {
    data class InlineContent(val blocks: List<ContentBlock>) : ParagraphBlock()
    data class QuoteContent(val blocks: List<ParagraphBlock>) : ParagraphBlock()
    data class VideoContent(val thumbnailUrl: String, val videoUrl: String, val title: String) : ParagraphBlock()
    data class ImageContent(val previewUrl: String, val originalUrl: String) : ParagraphBlock()
    data object Spacer : ParagraphBlock()
}
