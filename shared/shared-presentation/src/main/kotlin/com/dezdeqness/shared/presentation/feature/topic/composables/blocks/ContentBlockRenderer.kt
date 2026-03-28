package com.dezdeqness.shared.presentation.feature.topic.composables.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dezdeqness.shared.presentation.feature.topic.model.ParagraphBlock

private const val MAX_PREVIEW_PARAGRAPHS = 2
private const val MAX_PREVIEW_TEXT_LINES = 6

@Composable
fun ContentBlockRenderer(
    blocks: List<ParagraphBlock>,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
) {
    Column(modifier = modifier) {
        var paragraphCount = 0
        for (paragraph in blocks) {
            when (paragraph) {
                is ParagraphBlock.InlineContent -> {
                    if (isPreview && paragraphCount >= MAX_PREVIEW_PARAGRAPHS) break
                    InlineContentBlock(
                        blocks = paragraph.blocks,
                        maxLines = if (isPreview) MAX_PREVIEW_TEXT_LINES else Int.MAX_VALUE,
                        overflow = if (isPreview) TextOverflow.Ellipsis else TextOverflow.Clip,
                    )
                    paragraphCount++
                }

                is ParagraphBlock.QuoteContent -> {
                    if (isPreview) continue
                    QuoteBlock(blocks = paragraph.blocks)
                }

                is ParagraphBlock.VideoContent -> {
                    VideoBlock(thumbnailUrl = paragraph.thumbnailUrl)
                }

                is ParagraphBlock.ImageContent -> {
                    ImageBlock(previewUrl = paragraph.previewUrl)
                }

                is ParagraphBlock.Spacer -> {
                    if (isPreview && paragraphCount >= MAX_PREVIEW_PARAGRAPHS) break
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
