package com.dezdeqness.feature.news.presentation.composables.blocks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.news.presentation.models.ContentBlock
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun InlineContentBlock(
    blocks: List<ContentBlock>,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    val annotatedString = buildInlineAnnotatedString(blocks)
    if (annotatedString.isNotEmpty()) {
        Text(
            text = annotatedString,
            color = AppTheme.colors.textPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modifier,
        )
    }
}

@Composable
private fun buildInlineAnnotatedString(blocks: List<ContentBlock>) = buildAnnotatedString {
    for (block in blocks) {
        when (block) {
            is ContentBlock.Text -> {
                appendSpaceIfNeeded(block.text)
                val style = SpanStyle(
                    fontWeight = if (block.bold) FontWeight.Bold else null,
                    fontStyle = if (block.italic) FontStyle.Italic else null,
                    textDecoration = if (block.underline) TextDecoration.Underline else null,
                )
                withStyle(style) {
                    append(block.text)
                }
            }

            is ContentBlock.Link -> {
                appendSpaceIfNeeded(block.text)
                withStyle(SpanStyle(color = AppTheme.colors.accent)) {
                    append(block.text)
                }
            }

            is ContentBlock.LineBreak -> {
                append("\n")
            }

            else -> {}
        }
    }
}

private fun AnnotatedString.Builder.appendSpaceIfNeeded(nextText: String) {
    if (length == 0 || nextText.isEmpty()) return
    val lastChar = toAnnotatedString().text.last()
    val firstChar = nextText.first()
    if (lastChar.isWhitespace() || firstChar.isWhitespace()) return
    if (firstChar in "»)].,:;!?") return
    if (lastChar in "«([") return
    if (lastChar.isLetterOrDigit() || firstChar.isLetterOrDigit()) {
        append(" ")
    }
}
