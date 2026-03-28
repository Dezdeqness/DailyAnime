package com.dezdeqness.shared.presentation.feature.topic

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.shared.presentation.feature.topic.model.ContentBlock
import com.dezdeqness.shared.presentation.feature.topic.model.ParagraphBlock
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel
import com.dezdeqness.shared.presentation.feature.topic.parser.FooterParser
import com.dezdeqness.shared.presentation.feature.topic.parser.HtmlParser
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class TopicPresentationComposer @Inject constructor(
    private val htmlParser: HtmlParser,
    private val footerParser: FooterParser,
) {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())

    fun compose(topic: TopicEntity): TopicPresentationModel {
        val linked = topic.linked.takeIf { topic.linkedType == LINKED_TYPE_ANIME }

        return TopicPresentationModel(
            topicId = topic.id,
            title = topic.topicTitle,
            userNickname = topic.userNickname,
            userAvatarUrl = topic.userAvatarUrl,
            date = formatDate(topic.createdAt),
            commentsCount = topic.commentsCount,
            contentBlocks = groupIntoParagraphs(htmlParser.parse(topic.htmlBody)),
            footerBlocks = groupIntoParagraphs(footerParser.parse(topic.htmlFooter)),
            linkedTitle = linked?.russian?.ifEmpty { linked.name },
            linkedImageUrl = linked?.image
                ?.let { it.preview.ifEmpty { it.original } }
                ?.normalizeUrl(),
            linkedId = topic.linkedId,
        )
    }

    fun compose(topics: List<TopicEntity>): List<TopicPresentationModel> = topics.map(::compose)

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
                    is ContentBlock.Video -> ParagraphBlock.VideoContent(
                        thumbnailUrl = block.thumbnailUrl,
                        videoUrl = block.videoUrl,
                        title = block.title,
                    )
                    is ContentBlock.Image -> ParagraphBlock.ImageContent(
                        previewUrl = block.previewUrl,
                        originalUrl = block.originalUrl,
                    )
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

    private companion object {
        const val LINKED_TYPE_ANIME = "Anime"
    }
}
