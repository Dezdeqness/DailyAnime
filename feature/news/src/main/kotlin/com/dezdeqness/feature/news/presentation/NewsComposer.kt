package com.dezdeqness.feature.news.presentation

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.feature.news.presentation.models.ContentBlock
import com.dezdeqness.feature.news.presentation.models.NewsUiModel
import com.dezdeqness.feature.news.presentation.models.ParagraphBlock
import com.dezdeqness.feature.news.presentation.parser.FooterParser
import com.dezdeqness.feature.news.presentation.parser.HtmlParser
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class NewsComposer @Inject constructor(
    private val htmlParser: HtmlParser,
    private val footerParser: FooterParser,
) {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())

    fun compose(topics: List<TopicEntity>): List<NewsUiModel> {
        return topics.map { topic ->
            NewsUiModel.NewsItem(
                topicId = topic.id,
                title = topic.topicTitle,
                userNickname = topic.userNickname,
                userAvatarUrl = topic.userAvatarUrl,
                date = formatDate(topic.createdAt),
                commentsCount = topic.commentsCount,
                contentBlocks = groupIntoParagraphs(htmlParser.parse(topic.htmlBody)),
                footerBlocks = groupIntoParagraphs(footerParser.parse(topic.htmlFooter)),
                linkedTitle = topic.linked?.russian?.ifEmpty { topic.linked?.name },
                linkedImageUrl = topic.linked?.image
                    ?.let { it.preview.ifEmpty { it.original } }
                    ?.normalizeUrl(),
                linkedId = topic.linkedId,
            )
        }
    }

    private fun String.normalizeUrl(): String =
        when {
            startsWith("//") -> "https:$this"
            startsWith("/") -> "https://shikimori.one$this"
            else -> this
        }

    private fun groupIntoParagraphs(blocks: List<ContentBlock>): List<ParagraphBlock> =
        buildList {
            val currentInline = mutableListOf<ContentBlock>()

            fun flushInline() {
                if (currentInline.isNotEmpty()) {
                    add(ParagraphBlock.InlineContent(currentInline.toList()))
                    currentInline.clear()
                }
            }

            for (block in blocks) {
                val paragraph = when (block) {
                    is ContentBlock.ParagraphBreak -> ParagraphBlock.Spacer
                    is ContentBlock.Quote -> ParagraphBlock.QuoteContent(groupIntoParagraphs(block.blocks))
                    is ContentBlock.Video -> ParagraphBlock.VideoContent(block.thumbnailUrl, block.videoUrl, block.title)
                    is ContentBlock.Image -> ParagraphBlock.ImageContent(block.previewUrl, block.originalUrl)
                    else -> {
                        currentInline.add(block)
                        null
                    }
                }
                if (paragraph != null) {
                    flushInline()
                    add(paragraph)
                }
            }
            flushInline()
        }

    private fun formatDate(dateString: String): String {
        return try {
            val date = inputDateFormat.parse(dateString) ?: return dateString
            outputDateFormat.format(date)
        } catch (_: Exception) {
            dateString
        }
    }
}
